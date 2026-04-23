import { getEndpointsForModule, getModuleById } from '../../config/api.config';
import { EndpointDefinition, ModuleDefinition } from '../../models/module.model';

// This file keeps book feature values in one easy place.
export const BOOKS_MODULE_ID = 'book';
export const BOOKS_ROUTE = '/book';

export const BOOKS_MODULE: ModuleDefinition | undefined = getModuleById(BOOKS_MODULE_ID);
export const BOOKS_ENDPOINTS: EndpointDefinition[] = getEndpointsForModule(BOOKS_MODULE_ID);
