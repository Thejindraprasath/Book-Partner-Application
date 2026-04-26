import { getEndpointsForModule, getModuleById } from '../../config/api.config';
import { EndpointDefinition, ModuleDefinition } from '../../models/module.model';

// Keep store-specific IDs, routes, and endpoint lists in one place.
export const STORES_MODULE_ID = 'store';
export const STORES_ROUTE = '/store';

// Read the module details and endpoint list from the central config.
export const STORES_MODULE: ModuleDefinition | undefined = getModuleById(STORES_MODULE_ID);
export const STORES_ENDPOINTS: EndpointDefinition[] = getEndpointsForModule(STORES_MODULE_ID);
