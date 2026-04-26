
import { getEndpointsForModule } from '../../config/api.config';
import { EndpointDefinition } from '../../models/module.model';

// Employees live inside the Sanjai area, so their visible route is nested.
export const EMPLOYEES_MODULE_ID = 'employee';
export const EMPLOYEES_ROUTE = '/sanjai/employees';
export const EMPLOYEES_PAGE_TITLE = 'Employee Dashboard';
export const EMPLOYEES_PAGE_DESCRIPTION =
  "Employee and job endpoints available inside Sanjai's module.";

// Load the employee endpoint list from the shared config.
export const EMPLOYEES_ENDPOINTS: EndpointDefinition[] =
  getEndpointsForModule(EMPLOYEES_MODULE_ID);
