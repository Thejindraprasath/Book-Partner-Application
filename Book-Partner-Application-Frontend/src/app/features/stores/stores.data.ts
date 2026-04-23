import { getEndpointsForModule, getModuleById } from '../../config/api.config';
import { EndpointDefinition, ModuleDefinition } from '../../models/module.model';

// This file keeps store feature values in one easy place.
export const STORES_MODULE_ID = 'store';
export const STORES_ROUTE = '/store';

export const STORES_MODULE: ModuleDefinition | undefined = getModuleById(STORES_MODULE_ID);
export const STORES_ENDPOINTS: EndpointDefinition[] = getEndpointsForModule(STORES_MODULE_ID);
