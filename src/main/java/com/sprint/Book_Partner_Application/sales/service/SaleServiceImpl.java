package com.sprint.Book_Partner_Application.sales.service;

import com.sprint.Book_Partner_Application.book.entity.Title;
import com.sprint.Book_Partner_Application.book.repository.TitleRepository;
import com.sprint.Book_Partner_Application.exception.ResourceNotFoundException;
import com.sprint.Book_Partner_Application.sales.entity.Sale;
import com.sprint.Book_Partner_Application.sales.repository.SaleRepository;
import com.sprint.Book_Partner_Application.store.entity.Store;
import com.sprint.Book_Partner_Application.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final StoreRepository storeRepository;
    private final TitleRepository titleRepository;

    // ─── CREATE ─────────────────────────────────────────────────────────────
    public Sale createSale(Sale sale) {

        // ✅ Build composite ID manually
        Sale.SaleId id = new Sale.SaleId(
                sale.getStorId(),
                sale.getOrdNum(),
                sale.getTitleId()
        );

        // Duplicate check
        if (saleRepository.existsById(id)) {
            throw new DuplicateResourceException(
                    "Sale",
                    "composite key (storId, ordNum, titleId)",
                    id
            );
        }

        // Validate Store
        Store store = storeRepository.findById(sale.getStorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Store", "storId", sale.getStorId()));

        // Validate Title
        Title title = titleRepository.findById(sale.getTitleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Title", "titleId", sale.getTitleId()));

        // Business rule (optional because @Min already exists)
        if (sale.getQty() == null || sale.getQty() <= 0) {
            throw new BusinessValidationException("qty", "must be greater than 0");
        }

        // Set relationships (optional but good practice)
        sale.setStore(store);
        sale.setTitle(title);

        return saleRepository.save(sale);
    }

    // ─── GET BY ID ──────────────────────────────────────────────────────────
    public Sale getSaleById(String storId, String ordNum, String titleId) {

        Sale.SaleId id = new Sale.SaleId(storId, ordNum, titleId);

        return saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sale", "id", id));
    }

    // ─── UPDATE ─────────────────────────────────────────────────────────────
    public Sale updateSale(String storId, String ordNum, String titleId, Sale updatedSale) {

        Sale.SaleId id = new Sale.SaleId(storId, ordNum, titleId);

        Sale existing = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sale", "id", id));

        if (updatedSale.getQty() == null || updatedSale.getQty() <= 0) {
            throw new BusinessValidationException("qty", "must be greater than 0");
        }

        existing.setQty(updatedSale.getQty());
        existing.setPayterms(updatedSale.getPayterms());
        existing.setOrdDate(updatedSale.getOrdDate());

        return saleRepository.save(existing);
    }

    // ─── DELETE ─────────────────────────────────────────────────────────────
    public void deleteSale(String storId, String ordNum, String titleId) {

        Sale.SaleId id = new Sale.SaleId(storId, ordNum, titleId);

        Sale existing = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sale", "id", id));

        try {
            saleRepository.delete(existing);
        } catch (Exception ex) {
            throw new ResourceInUseException(
                    "Sale", id, "dependent records");
        }
    }
}
