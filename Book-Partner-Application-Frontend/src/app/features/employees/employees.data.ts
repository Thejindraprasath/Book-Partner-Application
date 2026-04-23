
import { getEndpointsForModule } from '../../config/api.config';
import { EndpointDefinition } from '../../models/module.model';

// Employees live inside the Sanjai area, so their visible route is a nested path.
export const EMPLOYEES_MODULE_ID = 'employee';
export const EMPLOYEES_ROUTE = '/sanjai/employees';
export const EMPLOYEES_PAGE_TITLE = 'Employee Dashboard';
export const EMPLOYEES_PAGE_DESCRIPTION =
  "Employee and job endpoints available inside Sanjai's module.";

export const EMPLOYEES_ENDPOINTS: EndpointDefinition[] =
  getEndpointsForModule(EMPLOYEES_MODULE_ID);
