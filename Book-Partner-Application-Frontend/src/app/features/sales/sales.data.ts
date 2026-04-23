import { getEndpointsForModule, getModuleById } from '../../config/api.config';
import { EndpointDefinition, ModuleDefinition } from '../../models/module.model';

// Keep sales-specific values in one place so the other files stay short and easy to read.
export const SALES_MODULE_ID = 'sales';
export const SALES_ROUTE = '/sales';

// The config file already contains the real data. We read it once here and reuse it everywhere.
export const SALES_MODULE: ModuleDefinition | undefined = getModuleById(SALES_MODULE_ID);
export const SALES_ENDPOINTS: EndpointDefinition[] = getEndpointsForModule(SALES_MODULE_ID);
