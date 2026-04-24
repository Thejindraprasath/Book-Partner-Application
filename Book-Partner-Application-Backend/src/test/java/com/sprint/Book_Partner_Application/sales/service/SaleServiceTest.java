package com.sprint.Book_Partner_Application.sales.service;

import com.sprint.Book_Partner_Application.book.entity.Title;
import com.sprint.Book_Partner_Application.book.repository.TitleRepository;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.sales.dto.request.SaleCreateRequest;
import com.sprint.Book_Partner_Application.sales.dto.response.SaleResponse;
import com.sprint.Book_Partner_Application.sales.entity.Sale;
import com.sprint.Book_Partner_Application.sales.exception.*;
import com.sprint.Book_Partner_Application.sales.repository.SaleRepository;
import com.sprint.Book_Partner_Application.store.entity.Store;
import com.sprint.Book_Partner_Application.store.repository.StoreRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private TitleRepository titleRepository;

    @InjectMocks
    private SaleServiceImpl saleService;

    private Sale sale;
    private SaleCreateRequest request;

    @BeforeEach
    void setup() {

        // DTO can still use builder
        request = SaleCreateRequest.builder()
                .storId("S1")
                .ordNum("O1")
                .titleId("T1")
                .qty((short) 5)
                .ordDate(LocalDateTime.now().minusDays(1))
                .payterms("Net 30")
                .build();

        // ❌ Removed builder → ✅ Use setters
        sale = new Sale();
        sale.setStorId("S1");
        sale.setOrdNum("O1");
        sale.setTitleId("T1");
        sale.setQty((short) 5);
        sale.setOrdDate(LocalDateTime.now().minusDays(1));
        sale.setPayterms("Net 30");
    }

    // ================= POSITIVE =================

    @Test
    void createSale_success() {
        when(storeRepository.findById(any()))
                .thenReturn(Optional.of(new Store()));

        when(titleRepository.findById(any()))
                .thenReturn(Optional.of(new Title()));

        when(saleRepository.existsById(any())).thenReturn(false);
        when(saleRepository.save(any())).thenReturn(sale);

        SaleResponse response = saleService.createSale(request);

        assertNotNull(response);
    }

    @Test
    void getAllSales_success() {
        Page<Sale> page = new PageImpl<>(List.of(sale));

        when(saleRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        PageResponse<SaleResponse> response =
                saleService.getAllSales(PageRequest.of(0, 10));

        assertEquals(1, response.getContent().size());
    }

    @Test
    void getSaleById_success() {
        when(storeRepository.findById(any()))
                .thenReturn(Optional.of(new Store()));

        when(titleRepository.findById(any()))
                .thenReturn(Optional.of(new Title()));

        when(saleRepository.findById(any()))
                .thenReturn(Optional.of(sale));

        SaleResponse response =
                saleService.getSaleById("S1", "O1", "T1");

        assertNotNull(response);
    }

    @Test
    void getSalesByBranch_success() {
        when(storeRepository.findById(any()))
                .thenReturn(Optional.of(new Store()));

        when(saleRepository.findByStorId("S1"))
                .thenReturn(List.of(sale));

        List<SaleResponse> list =
                saleService.getSalesByBranch("S1");

        assertEquals(1, list.size());
    }

    @Test
    void getSalesByProduct_success() {
        when(titleRepository.findById(any()))
                .thenReturn(Optional.of(new Title()));

        when(saleRepository.findByTitleId("T1"))
                .thenReturn(List.of(sale));

        List<SaleResponse> list =
                saleService.getSalesByProduct("T1");

        assertEquals(1, list.size());
    }

    @Test
    void getSalesByDateRange_success() {
        when(saleRepository.findByDateRange(any(), any()))
                .thenReturn(List.of(sale));

        List<SaleResponse> list =
                saleService.getSalesByDateRange(
                        LocalDateTime.now().minusDays(5),
                        LocalDateTime.now()
                );

        assertFalse(list.isEmpty());
    }

    // ================= NEGATIVE =================

    @Test
    void createSale_alreadyExists() {
        when(storeRepository.findById(any()))
                .thenReturn(Optional.of(new Store()));

        when(titleRepository.findById(any()))
                .thenReturn(Optional.of(new Title()));

        when(saleRepository.existsById(any())).thenReturn(true);

        assertThrows(SaleAlreadyExistsException.class, () ->
                saleService.createSale(request)
        );
    }

    @Test
    void createSale_invalidQuantity() {
        when(storeRepository.findById(any()))
                .thenReturn(Optional.of(new Store()));

        when(titleRepository.findById(any()))
                .thenReturn(Optional.of(new Title()));

        request.setQty((short) 0);

        assertThrows(InvalidSaleQuantityException.class, () ->
                saleService.createSale(request)
        );
    }

    @Test
    void createSale_futureDate() {
        when(storeRepository.findById(any()))
                .thenReturn(Optional.of(new Store()));

        when(titleRepository.findById(any()))
                .thenReturn(Optional.of(new Title()));

        request.setOrdDate(LocalDateTime.now().plusDays(1));

        assertThrows(FutureSaleDateException.class, () ->
                saleService.createSale(request)
        );
    }

    @Test
    void createSale_invalidPayterms() {
        when(storeRepository.findById(any()))
                .thenReturn(Optional.of(new Store()));

        when(titleRepository.findById(any()))
                .thenReturn(Optional.of(new Title()));

        request.setPayterms("INVALID");

        assertThrows(InvalidPaytermsException.class, () ->
                saleService.createSale(request)
        );
    }

    @Test
    void getSaleById_notFound() {
        when(storeRepository.findById(any()))
                .thenReturn(Optional.of(new Store()));

        when(titleRepository.findById(any()))
                .thenReturn(Optional.of(new Title()));

        when(saleRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThrows(SaleNotFoundException.class, () ->
                saleService.getSaleById("S1", "O1", "T1")
        );
    }

    @Test
    void getSalesByDateRange_invalidRange() {
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.now().minusDays(1);

        assertThrows(InvalidSaleDateRangeException.class, () ->
                saleService.getSalesByDateRange(from, to)
        );
    }

    @Test
    void getSalesByDateRange_nullDates() {
        assertThrows(InvalidSaleDateRangeException.class, () ->
                saleService.getSalesByDateRange(null, null)
        );
    }

    @Test
    void getSalesByDateRange_tooWide() {
        LocalDateTime from = LocalDateTime.now().minusYears(10);
        LocalDateTime to = LocalDateTime.now();

        assertThrows(InvalidSaleDateRangeException.class, () ->
                saleService.getSalesByDateRange(from, to)
        );
    }
}