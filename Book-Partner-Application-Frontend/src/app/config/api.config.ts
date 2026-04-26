import { EndpointDefinition, ModuleDefinition } from '../models/module.model';

export const API_BASE_URL = 'http://localhost:8081';

export const APP_MODULES: ModuleDefinition[] = [
  {
    id: 'store',
    label: 'Vennila-Store',
    description: 'Handles store operations including branch management, discount application, and transaction processing. Ensures smooth coordination of store-level activities and data consistency.',
    loginHint: 'Use Vennila store credentials.',
    route: '/store',
    roles: ['ROLE_STORE'],
    accent: 'from-emerald-500 to-teal-600',
  },
  {
    id: 'author',
    label: 'Akalya-Author',
    description: 'Manages author information and maintains relationships between authors and book titles. Supports accurate tracking of author contributions across publications.',
    loginHint: 'Use Akalya author credentials.',
    route: '/author',
    roles: ['ROLE_AUTHOR'],
    accent: 'from-amber-500 to-orange-600',
  },
  {
    id: 'book',
    label: 'Sachitha-Book',
    description: 'Manages book titles and related data, including pricing, publication details, and royalty structures. Ensures proper organization and integrity of all book-related records within the system.',
    loginHint: 'Use Sachitha book credentials.',
    route: '/book',
    roles: ['ROLE_BOOK'],
    accent: 'from-sky-500 to-cyan-600',
  },
  {
    id: 'sanjai',
    label: 'Sanjai-Publisher-Employee',
    description: 'Handles publisher details and employee management within publishing operations. Provides a unified interface to manage organizational and operational data efficiently.',
    loginHint: 'Use Sanjai credentials.',
    route: '/sanjai',
    roles: ['ROLE_PUBLISHER', 'ROLE_EMPLOYEE'],
    accent: 'from-rose-500 to-pink-600',
  },
  {
    id: 'sales',
    label: 'Theja-Sales',
    description: 'Manages sales transactions and generates sales reports. Ensures accurate tracking of sales data and supports business insights through reporting.',
    loginHint: 'Use Theja sales credentials.',
    route: '/sales',
    roles: ['ROLE_SALE'],
    accent: 'from-violet-500 to-fuchsia-600',
  },
];

export const MODULE_ENDPOINTS: Record<string, EndpointDefinition[]> = {
  store: [
    {
      id: 'listStores',
      title: 'Get All Stores',
      description: 'Fetch all stores in table format.',
      route: 'stores',
      apiPath: '/api/v1/stores',
      method: 'GET',
      formFields: []
    },
    {
      id: 'getStoreById',
      title: 'Get Store By ID',
      description: 'Fetch one store by its store ID.',
      route: 'stores/by-id',
      apiPath: '/api/v1/stores/{storeId}',
      method: 'GET',
      formFields: [{ name: 'storeId', label: 'Store ID', type: 'text', location: 'path', required: true }]
    },
    {
      id: 'createStore',
      title: 'Create Store',
      description: 'Create a new store record.',
      route: 'stores/create',
      apiPath: '/api/v1/stores',
      method: 'POST',
      formFields: [
        { name: 'storId', label: 'Store ID', type: 'text', location: 'body', required: true },
        { name: 'storName', label: 'Store Name', type: 'text', location: 'body', required: true },
        { name: 'storAddress', label: 'Address', type: 'text', location: 'body', required: true },
        { name: 'city', label: 'City', type: 'text', location: 'body', required: true },
        { name: 'state', label: 'State', type: 'text', location: 'body', required: true },
        { name: 'zip', label: 'Zip', type: 'text', location: 'body', required: true }
      ]
    },
    {
      id: 'updateStore',
      title: 'Update Store',
      description: 'Update an existing store.',
      route: 'stores/update',
      apiPath: '/api/v1/stores/{storeId}',
      method: 'PUT',
      formFields: [
        { name: 'storeId', label: 'Store ID', type: 'text', location: 'path', required: true },
        { name: 'storName', label: 'Store Name', type: 'text', location: 'body' },
        { name: 'storAddress', label: 'Address', type: 'text', location: 'body' },
        { name: 'city', label: 'City', type: 'text', location: 'body' },
        { name: 'state', label: 'State', type: 'text', location: 'body' },
        { name: 'zip', label: 'Zip', type: 'text', location: 'body' }
      ]
    },
    {
      id: 'deleteStore',
      title: 'Delete Store',
      description: 'Delete a store by ID.',
      route: 'stores/delete',
      apiPath: '/api/v1/stores/{storeId}',
      method: 'DELETE',
      formFields: [{ name: 'storeId', label: 'Store ID', type: 'text', location: 'path', required: true }]
    },
    {
      id: 'storeTransactions',
      title: 'Get Store Transactions',
      description: 'Fetch transactions for one store.',
      route: 'stores/transactions',
      apiPath: '/api/v1/stores/{storeId}/transactions',
      method: 'GET',
      formFields: [{ name: 'storeId', label: 'Store ID', type: 'text', location: 'path', required: true }]
    },
    {
      id: 'storeDiscounts',
      title: 'Get Store Discounts',
      description: 'Fetch discounts for one store.',
      route: 'stores/discounts',
      apiPath: '/api/v1/stores/{storeId}/discounts',
      method: 'GET',
      formFields: [
        {
          name: 'storeId',
          label: 'Store ID',
          type: 'text',
          location: 'path',
          required: true
        }
      ]
    },
    {
      id: 'listDiscounts',
      title: 'Get All Discounts',
      description: 'Fetch all discount records.',
      route: 'discounts',
      apiPath: '/api/v1/discounts',
      method: 'GET',
      formFields: []
    },
    {
      id: 'createDiscount',
      title: 'Create Discount',
      description: 'Create a new discount record.',
      route: 'discounts/create',
      apiPath: '/api/v1/discounts',
      method: 'POST',
      formFields: [
        { name: 'discountType', label: 'Discount Type', type: 'text', location: 'body', required: true },
        { name: 'storId', label: 'Store ID', type: 'text', location: 'body', required: true },
        { name: 'lowqty', label: 'Low Quantity', type: 'number', location: 'body', required: true },
        { name: 'highqty', label: 'High Quantity', type: 'number', location: 'body', required: true },
        { name: 'discount', label: 'Discount', type: 'number', location: 'body', required: true }
      ]
    },
    {
      id: 'getDiscountByType',
      title: 'Get Discount By Type',
      description: 'Fetch discount by type.',
      route: 'discounts/by-type',
      apiPath: '/api/v1/discounts/{discountType}',
      method: 'GET',
      formFields: [
        { name: 'discountType', label: 'Discount Type', type: 'text', location: 'path', required: true }
      ]
    },
    {
      id: 'updateDiscount',
      title: 'Update Discount',
      description: 'Update discount details.',
      route: 'discounts/update',
      apiPath: '/api/v1/discounts/{discountId}',
      method: 'PUT',
      formFields: [
        { name: 'discountId', label: 'Discount ID', type: 'number', location: 'path', required: true },
        { name: 'discountType', label: 'Discount Type', type: 'text', location: 'body' },
        { name: 'storId', label: 'Store ID', type: 'text', location: 'body' },
        { name: 'lowqty', label: 'Low Quantity', type: 'number', location: 'body' },
        { name: 'highqty', label: 'High Quantity', type: 'number', location: 'body' },
        { name: 'discount', label: 'Discount', type: 'number', location: 'body' }
      ]
    }
  ],
  author: [
    {
      id: 'listAuthors',
      title: 'Get All Authors',
      description: 'Fetch all authors in table format.',
      route: 'authors',
      apiPath: '/api/v1/authors',
      method: 'GET',
      formFields: []
    },
    {
      id: 'getAuthorById',
      title: 'Get Author By ID',
      description: 'Fetch one author by author ID.',
      route: 'authors/by-id',
      apiPath: '/api/v1/authors/{auId}',
      method: 'GET',
      formFields: [{ name: 'auId', label: 'Author ID', type: 'text', location: 'path', required: true }]
    },
    {
      id: 'createAuthor',
      title: 'Create Author',
      description: 'Create a new author.',
      route: 'authors/create',
      apiPath: '/api/v1/authors',
      method: 'POST',
      formFields: [
        { name: 'auId', label: 'Author ID', type: 'text', location: 'body', required: true },
        { name: 'auLname', label: 'Last Name', type: 'text', location: 'body', required: true },
        { name: 'auFname', label: 'First Name', type: 'text', location: 'body', required: true },
        { name: 'phone', label: 'Phone', type: 'text', location: 'body', required: true },
        { name: 'address', label: 'Address', type: 'text', location: 'body' },
        { name: 'city', label: 'City', type: 'text', location: 'body' },
        { name: 'state', label: 'State', type: 'text', location: 'body' },
        { name: 'zip', label: 'Zip', type: 'text', location: 'body' },
        { name: 'contract', label: 'Contract', type: 'number', location: 'body', required: true }
      ]
    },
    {
      id: 'updateAuthor',
      title: 'Update Author',
      description: 'Update an author record.',
      route: 'authors/update',
      apiPath: '/api/v1/authors/{auId}',
      method: 'PUT',
      formFields: [
        { name: 'auId', label: 'Author ID', type: 'text', location: 'path', required: true },
        { name: 'auLname', label: 'Last Name', type: 'text', location: 'body' },
        { name: 'auFname', label: 'First Name', type: 'text', location: 'body' },
        { name: 'phone', label: 'Phone', type: 'text', location: 'body' },
        { name: 'address', label: 'Address', type: 'text', location: 'body' },
        { name: 'city', label: 'City', type: 'text', location: 'body' },
        { name: 'state', label: 'State', type: 'text', location: 'body' },
        { name: 'zip', label: 'Zip', type: 'text', location: 'body' },
        { name: 'contract', label: 'Contract', type: 'number', location: 'body' }
      ]
    },
    {
      id: 'deleteAuthor',
      title: 'Delete Author',
      description: 'Delete one author by ID.',
      route: 'authors/delete',
      apiPath: '/api/v1/authors/{auId}',
      method: 'DELETE',
      formFields: [{ name: 'auId', label: 'Author ID', type: 'text', location: 'path', required: true }]
    },
    {
      id: 'authorTitles',
      title: 'Get Titles By Author',
      description: 'Fetch title links for a specific author.',
      route: 'authors/titles',
      apiPath: '/api/v1/authors/{auId}/titles',
      method: 'GET',
      formFields: [{ name: 'auId', label: 'Author ID', type: 'text', location: 'path', required: true }]
    }
  ],
  book: [
    {
      id: 'listBooks',
      title: 'Get All Books',
      description: 'Fetch all books in table format.',
      route: 'books',
      apiPath: '/api/titles',
      method: 'GET',
      formFields: []
    },
    {
      id: 'getBookById',
      title: 'Get Book By ID',
      description: 'Fetch one title by ID.',
      route: 'books/by-id',
      apiPath: '/api/titles/{id}',
      method: 'GET',
      formFields: [
        { name: 'id', label: 'Title ID', type: 'text', location: 'path', required: true }
      ]
    },
    {
      id: 'createBook',
      title: 'Create Book',
      description: 'Create a title record.',
      route: 'books/create',
      apiPath: '/api/titles',
      method: 'POST',
      formFields: [
        { name: 'titleId', label: 'Title ID', type: 'text', location: 'body', required: true },
        { name: 'title', label: 'Title', type: 'text', location: 'body', required: true },
        { name: 'type', label: 'Type', type: 'text', location: 'body', required: true },
        { name: 'pubId', label: 'Publisher ID', type: 'text', location: 'body', required: true },
        { name: 'price', label: 'Price', type: 'number', location: 'body', required: true },
        { name: 'advance', label: 'Advance', type: 'number', location: 'body' },
        { name: 'royalty', label: 'Royalty', type: 'number', location: 'body' },
        { name: 'ytdSales', label: 'YTD Sales', type: 'number', location: 'body' },
        { name: 'notes', label: 'Notes', type: 'text', location: 'body' },
        { name: 'pubdate', label: 'Published Date', type: 'datetime-local', location: 'body', required: true }
      ]
    },
    {
      id: 'updateBook',
      title: 'Update Book',
      description: 'Update an existing title.',
      route: 'books/update',
      apiPath: '/api/titles/{id}',
      method: 'PUT',
      formFields: [
        { name: 'id', label: 'Title ID', type: 'text', location: 'path', required: true },
        { name: 'title', label: 'Title', type: 'text', location: 'body' },
        { name: 'type', label: 'Type', type: 'text', location: 'body' },
        { name: 'pubId', label: 'Publisher ID', type: 'text', location: 'body' },
        { name: 'price', label: 'Price', type: 'number', location: 'body' },
        { name: 'advance', label: 'Advance', type: 'number', location: 'body' },
        { name: 'royalty', label: 'Royalty', type: 'number', location: 'body' },
        { name: 'ytdSales', label: 'YTD Sales', type: 'number', location: 'body' },
        { name: 'notes', label: 'Notes', type: 'text', location: 'body' },
        { name: 'pubdate', label: 'Published Date', type: 'datetime-local', location: 'body' }
      ]
    },
    {
      id: 'deleteBook',
      title: 'Delete Book',
      description: 'Delete one title by ID.',
      route: 'books/delete',
      apiPath: '/api/titles/{id}',
      method: 'DELETE',
      formFields: [
        { name: 'id', label: 'Title ID', type: 'text', location: 'path', required: true }
      ]
    },
    {
      id: 'getAuthorsByTitle',
      title: 'Get Authors By Title',
      description: 'Fetch all authors linked to a specific title.',
      route: 'books/authors',
      apiPath: '/api/titles/{id}/authors',
      method: 'GET',
      formFields: [
        { name: 'id', label: 'Title ID', type: 'text', location: 'path', required: true }
      ]
    },
    {
      id: 'createRoySched',
      title: 'Create Royalty Schedule',
      description: 'Create royalty schedule for a title.',
      route: 'books/roysched/create',
      apiPath: '/api/titles/roysched',
      method: 'POST',
      formFields: [
        { name: 'titleId', label: 'Title ID', type: 'text', location: 'body', required: true },
        { name: 'lorange', label: 'Low Range', type: 'number', location: 'body', required: true },
        { name: 'hirange', label: 'High Range', type: 'number', location: 'body', required: true },
        { name: 'royalty', label: 'Royalty', type: 'number', location: 'body', required: true }
      ]
    },
    {
      id: 'updateRoySched',
      title: 'Update Royalty Schedule',
      description: 'Update an existing royalty schedule.',
      route: 'books/roysched/update',
      apiPath: '/api/titles/roysched/{id}',
      method: 'PUT',
      formFields: [
        { name: 'id', label: 'Royalty Schedule ID', type: 'number', location: 'path', required: true },
        { name: 'lorange', label: 'Low Range', type: 'number', location: 'body' },
        { name: 'hirange', label: 'High Range', type: 'number', location: 'body' },
        { name: 'royalty', label: 'Royalty', type: 'number', location: 'body' }
      ]
    }
  ],
  publisher: [
    {
      id: 'listPublishers',
      title: 'Get All Publishers',
      description: 'Fetch all publishers in table format.',
      route: 'publishers',
      apiPath: '/api/publishers',
      method: 'GET',
      formFields: []
    },
    {
      id: 'getPublisherById',
      title: 'Get Publisher By ID',
      description: 'Fetch one publisher by ID.',
      route: 'publishers/by-id',
      apiPath: '/api/publishers/{pubId}',
      method: 'GET',
      formFields: [{ name: 'pubId', label: 'Publisher ID', type: 'text', location: 'path', required: true }]
    },
    {
      id: 'createPublisher',
      title: 'Create Publisher',
      description: 'Create a new publisher.',
      route: 'publishers/create',
      apiPath: '/api/publishers',
      method: 'POST',
      formFields: [
        { name: 'pubId', label: 'Publisher ID', type: 'text', location: 'body', required: true },
        { name: 'pubName', label: 'Publisher Name', type: 'text', location: 'body', required: true },
        { name: 'city', label: 'City', type: 'text', location: 'body', required: true },
        { name: 'state', label: 'State', type: 'text', location: 'body', required: true },
        { name: 'country', label: 'Country', type: 'text', location: 'body', required: true }
      ]
    },
    {
      id: 'updatePublisher',
      title: 'Update Publisher',
      description: 'Update publisher details.',
      route: 'publishers/update',
      apiPath: '/api/publishers/{pubId}',
      method: 'PUT',
      formFields: [
        { name: 'pubId', label: 'Publisher ID', type: 'text', location: 'path', required: true },
        { name: 'pubName', label: 'Publisher Name', type: 'text', location: 'body' },
        { name: 'city', label: 'City', type: 'text', location: 'body' },
        { name: 'state', label: 'State', type: 'text', location: 'body' },
        { name: 'country', label: 'Country', type: 'text', location: 'body' }
      ]
    },
    {
      id: 'deletePublisher',
      title: 'Delete Publisher',
      description: 'Delete one publisher by ID.',
      route: 'publishers/delete',
      apiPath: '/api/publishers/{pubId}',
      method: 'DELETE',
      formFields: [{ name: 'pubId', label: 'Publisher ID', type: 'text', location: 'path', required: true }]
    },
    {
      id: 'publisherEmployees',
      title: 'Get Employees By Publisher',
      description: 'Fetch employees for one publisher.',
      route: 'publishers/employees',
      apiPath: '/api/publishers/{pubId}/employees',
      method: 'GET',
      formFields: [{ name: 'pubId', label: 'Publisher ID', type: 'text', location: 'path', required: true }]
    },
    {
      id: 'publisherTitles',
      title: 'Get Titles By Publisher',
      description: 'Fetch titles for one publisher.',
      route: 'publishers/titles',
      apiPath: '/api/publishers/{pubId}/titles',
      method: 'GET',
      formFields: [{ name: 'pubId', label: 'Publisher ID', type: 'text', location: 'path', required: true }]
    }
  ],
  employee: [
    {
      id: 'listEmployees',
      title: 'Get All Employees',
      description: 'Fetch all employees in table format.',
      route: 'employees',
      apiPath: '/api/employees',
      method: 'GET',
      formFields: []
    },
    {
      id: 'getEmployeeById',
      title: 'Get Employee By ID',
      description: 'Fetch one employee by ID.',
      route: 'employees/by-id',
      apiPath: '/api/employees/{empId}',
      method: 'GET',
      formFields: [{ name: 'empId', label: 'Employee ID', type: 'text', location: 'path', required: true }]
    },
    {
      id: 'createEmployee',
      title: 'Create Employee',
      description: 'Create a new employee.',
      route: 'employees/create',
      apiPath: '/api/employees',
      method: 'POST',
      formFields: [
        { name: 'empId', label: 'Employee ID', type: 'text', location: 'body', required: true },
        { name: 'fname', label: 'First Name', type: 'text', location: 'body', required: true },
        { name: 'minit', label: 'Middle Initial', type: 'text', location: 'body' },
        { name: 'lname', label: 'Last Name', type: 'text', location: 'body', required: true },
        { name: 'jobId', label: 'Job ID', type: 'number', location: 'body', required: true },
        { name: 'jobLvl', label: 'Job Level', type: 'number', location: 'body', required: true },
        { name: 'pubId', label: 'Publisher ID', type: 'text', location: 'body', required: true },
        { name: 'hireDate', label: 'Hire Date', type: 'datetime-local', location: 'body', required: true }
      ]
    },
    {
      id: 'updateEmployee',
      title: 'Update Employee',
      description: 'Update employee details.',
      route: 'employees/update',
      apiPath: '/api/employees/{empId}',
      method: 'PUT',
      formFields: [
        { name: 'empId', label: 'Employee ID', type: 'text', location: 'path', required: true },
        { name: 'fname', label: 'First Name', type: 'text', location: 'body' },
        { name: 'minit', label: 'Middle Initial', type: 'text', location: 'body' },
        { name: 'lname', label: 'Last Name', type: 'text', location: 'body' },
        { name: 'jobId', label: 'Job ID', type: 'number', location: 'body' },
        { name: 'jobLvl', label: 'Job Level', type: 'number', location: 'body' },
        { name: 'pubId', label: 'Publisher ID', type: 'text', location: 'body' },
        { name: 'hireDate', label: 'Hire Date', type: 'datetime-local', location: 'body' }
      ]
    },
    {
      id: 'deleteEmployee',
      title: 'Delete Employee',
      description: 'Delete one employee by ID.',
      route: 'employees/delete',
      apiPath: '/api/employees/{empId}',
      method: 'DELETE',
      formFields: [{ name: 'empId', label: 'Employee ID', type: 'text', location: 'path', required: true }]
    },
    {
      id: 'employeesByPublisher',
      title: 'Get Employees By Publisher',
      description: 'Fetch employees for one publisher.',
      route: 'employees/by-publisher',
      apiPath: '/api/employees/publisher/{pubId}',
      method: 'GET',
      formFields: [{ name: 'pubId', label: 'Publisher ID', type: 'text', location: 'path', required: true }]
    },
    {
      id: 'listJobs',
      title: 'Get All Jobs',
      description: 'Fetch all job records.',
      route: 'employees/jobs',
      apiPath: '/api/employees/jobs',
      method: 'GET',
      formFields: []
    },
    {
      id: 'createJob',
      title: 'Create Job',
      description: 'Create a new job.',
      route: 'employees/jobs/create',
      apiPath: '/api/employees/jobs',
      method: 'POST',
      formFields: [
        { name: 'jobDesc', label: 'Job Description', type: 'text', location: 'body', required: true },
        { name: 'minLvl', label: 'Minimum Level', type: 'number', location: 'body', required: true },
        { name: 'maxLvl', label: 'Maximum Level', type: 'number', location: 'body', required: true }
      ]
    }
    ,
    {
      id: 'getJobById',
      title: 'Get Job By ID',
      description: 'Fetch one job using job ID.',
      route: 'employees/jobs/by-id',
      apiPath: '/api/employees/jobs/{jobId}',
      method: 'GET',
      formFields: [
        { name: 'jobId', label: 'Job ID', type: 'number', location: 'path', required: true }
      ]
    },
    {
      id: 'updateJob',
      title: 'Update Job',
      description: 'Update an existing job.',
      route: 'employees/jobs/update',
      apiPath: '/api/employees/jobs/{jobId}',
      method: 'PUT',
      formFields: [
        { name: 'jobId', label: 'Job ID', type: 'number', location: 'path', required: true },
        { name: 'jobDesc', label: 'Job Description', type: 'text', location: 'body' },
        { name: 'minLvl', label: 'Minimum Level', type: 'number', location: 'body' },
        { name: 'maxLvl', label: 'Maximum Level', type: 'number', location: 'body' }
      ]
    }
  ],
  sales: [
    {
      id: 'listSales',
      title: 'Get All Sales',
      description: 'Fetch all sales in table format.',
      route: 'transactions',
      apiPath: '/api/v1/transactions',
      method: 'GET',
      formFields: []
    },
    {
      id: 'getSaleById',
      title: 'Get Sale By ID',
      description: 'Fetch one sale by order number, store ID, and title ID.',
      route: 'transactions/by-id',
      apiPath: '/api/v1/transactions/{ordNum}?storId={storId}&titleId={titleId}',
      method: 'GET',
      formFields: [
        { name: 'ordNum', label: 'Order Number', type: 'text', location: 'path', required: true },
        { name: 'storId', label: 'Store ID', type: 'text', location: 'query', required: true },
        { name: 'titleId', label: 'Title ID', type: 'text', location: 'query', required: true }
      ]
    },
    {
      id: 'createSale',
      title: 'Create Sale',
      description: 'Create a new sale transaction.',
      route: 'transactions/create',
      apiPath: '/api/v1/transactions',
      method: 'POST',
      formFields: [
        { name: 'storId', label: 'Store ID', type: 'text', location: 'body', required: true },
        { name: 'ordNum', label: 'Order Number', type: 'text', location: 'body', required: true },
        { name: 'ordDate', label: 'Order Date', type: 'datetime-local', location: 'body', required: true },
        { name: 'qty', label: 'Quantity', type: 'number', location: 'body', required: true },
        { name: 'payterms', label: 'Pay Terms', type: 'text', location: 'body', required: true },
        { name: 'titleId', label: 'Title ID', type: 'text', location: 'body', required: true }
      ]
    },
    {
      id: 'salesByBranch',
      title: 'Get Sales By Branch',
      description: 'Fetch sales for one branch.',
      route: 'transactions/branch',
      apiPath: '/api/v1/transactions/branch/{storId}',
      method: 'GET',
      formFields: [{ name: 'storId', label: 'Store ID', type: 'text', location: 'path', required: true }]
    },
    {
      id: 'salesByProduct',
      title: 'Get Sales By Product',
      description: 'Fetch sales for one title.',
      route: 'transactions/product',
      apiPath: '/api/v1/transactions/product/{titleId}',
      method: 'GET',
      formFields: [{ name: 'titleId', label: 'Title ID', type: 'text', location: 'path', required: true }]
    },
    {
      id: 'salesByDateRange',
      title: 'Get Sales By Date Range',
      description: 'Fetch sales within a date range.',
      route: 'transactions/date-range',
      apiPath: '/api/v1/transactions/date-range?from={from}&to={to}',
      method: 'GET',
      formFields: [
        { name: 'from', label: 'From', type: 'datetime-local', location: 'query', required: true },
        { name: 'to', label: 'To', type: 'datetime-local', location: 'query', required: true }
      ]
    }
  ]
};

export function getModuleById(moduleId: string | null | undefined): ModuleDefinition | undefined {
  return APP_MODULES.find((moduleItem) => moduleItem.id === moduleId);
}

export function getEndpointsForModule(moduleId: string): EndpointDefinition[] {
  return MODULE_ENDPOINTS[moduleId] ?? [];
}
