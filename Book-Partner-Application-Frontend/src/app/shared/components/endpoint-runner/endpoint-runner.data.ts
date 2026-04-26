// Lookup values used by the shared endpoint runner for labels, placeholders, and validation.

export const ENTITY_LABELS: Record<string, string> = {
  store: 'Store',
  author: 'Author',
  book: 'Book',
  publisher: 'Publisher',
  employee: 'Employee',
  sales: 'Sale',
};

export const FIELD_PLACEHOLDERS: Record<string, string> = {
  storeId: 'Enter store ID (4 digits), e.g. 7066',
  storId: 'Enter store ID (4 digits), e.g. 7066',
  auId: 'Enter author ID, e.g. 409-56-7008',
  pubId: 'Enter publisher ID (4 digits), e.g. 1389',
  titleId: 'Enter title ID, e.g. BU1032',
  id: 'Enter title ID, e.g. BU1032',
  empId: 'Enter employee ID, e.g. PTC11962M',
  ordNum: 'Enter order number, e.g. 423LL930',
  city: 'Enter city, e.g. Oakland',
  state: 'Enter state code, e.g. CA',
  zip: 'Enter ZIP code (5 digits), e.g. 94705',
  phone: 'Enter phone, e.g. 415 658-9932',
  contract: 'Enter contract value, e.g. 1',
  type: 'Enter type, e.g. business',
  price: 'Enter price, e.g. 19.99',
  advance: 'Enter advance, e.g. 5000',
  royalty: 'Enter royalty, e.g. 10',
  ytdSales: 'Enter YTD sales, e.g. 4095',
  jobId: 'Enter job ID, e.g. 5',
  jobLvl: 'Enter job level, e.g. 175',
  minLvl: 'Enter minimum level, e.g. 10',
  maxLvl: 'Enter maximum level, e.g. 250',
  qty: 'Enter quantity, e.g. 10',
  payterms: 'Enter pay terms, e.g. Net 60',
  page: 'Enter page number, e.g. 0',
  size: 'Enter page size, e.g. 10',
  direction: 'Enter asc or desc',
  sortBy: 'Enter sort field, e.g. ordDate',
  sort: 'Enter sort, e.g. title,asc',
  country: 'Enter country, e.g. USA',
  minit: 'Enter one letter, e.g. T',
  from: 'Select start date and time',
  to: 'Select end date and time',
  pubdate: 'Select date and time',
  ordDate: 'Select date and time',
  hireDate: 'Select date and time',
};

export const FIELD_PATTERNS: Record<string, RegExp> = {
  storeId: /^\d{4}$/,
  storId: /^\d{4}$/,
  pubId: /^\d{4}$/,
  auId: /^\d{3}-\d{2}-\d{4}$/,
  zip: /^\d{5}$/,
  state: /^[A-Z]{2}$/,
  titleId: /^[A-Z]{2}\d{4}$|^[A-Z]{2}\d{4,6}$|^[A-Z]{2,3}\d{4}$/,
  id: /^[A-Z]{2}\d{4}$|^[A-Z]{2}\d{4,6}$|^[A-Z]{2,3}\d{4}$/,
  empId: /^([A-Z]{3}[1-9][0-9]{4}[FM]|[A-Z]-[A-Z][1-9][0-9]{4}[FM])$/,
  minit: /^[A-Za-z]$/,
  contract: /^[01]$/,
};

export const FIELD_PATTERN_MESSAGES: Record<string, string> = {
  storeId: 'Store ID must be exactly 4 digits, e.g. 7066.',
  storId: 'Store ID must be exactly 4 digits, e.g. 7066.',
  pubId: 'Publisher ID must be exactly 4 digits, e.g. 1389.',
  auId: 'Author ID must match 999-99-9999 format.',
  zip: 'ZIP code must be exactly 5 digits.',
  state: 'State must be a 2-letter uppercase code, e.g. CA.',
  titleId: 'Title ID should look like BU1032, PS2091, or TC7777.',
  id: 'Title ID should look like BU1032, PS2091, or TC7777.',
  empId: 'Employee ID must match a valid format, e.g. PTC11962M.',
  minit: 'Middle Initial must be a single letter.',
  contract: 'Contract must be 0 or 1.',
};

export const FIELD_MIN_VALUES: Record<string, number> = {
  price: 0.01,
};

export const FIELD_MAX_VALUES: Record<string, number> = {
  royalty: 100,
  contract: 1,
};

export const FIELD_MIN_MESSAGES: Record<string, string> = {
  price: 'greater than 0',
  royalty: 'between 0 and 100',
  contract: '0 or 1',
};

export const FIELD_MAX_MESSAGES: Record<string, string> = {
  royalty: 'between 0 and 100',
  contract: '0 or 1',
};
