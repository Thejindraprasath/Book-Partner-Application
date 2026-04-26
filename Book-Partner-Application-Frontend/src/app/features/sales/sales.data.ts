import { getEndpointsForModule, getModuleById } from '../../config/api.config';
import { EndpointDefinition, ModuleDefinition } from '../../models/module.model';

// Keep sales-specific IDs, routes, and endpoint lists in one place.
export const SALES_MODULE_ID = 'sales';
export const SALES_ROUTE = '/sales';

// Read the module details and endpoint list from the central config.
export const SALES_MODULE: ModuleDefinition | undefined = getModuleById(SALES_MODULE_ID);
export const SALES_ENDPOINTS: EndpointDefinition[] = getEndpointsForModule(SALES_MODULE_ID);
