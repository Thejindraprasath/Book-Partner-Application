import { getEndpointsForModule } from '../../config/api.config';
import { EndpointDefinition } from '../../models/module.model';

// Publishers also live inside the Sanjai area, so their route is nested.
export const PUBLISHERS_MODULE_ID = 'publisher';
export const PUBLISHERS_ROUTE = '/sanjai/publishers';
export const PUBLISHERS_PAGE_TITLE = 'Publisher Dashboard';
export const PUBLISHERS_PAGE_DESCRIPTION =
  'Publisher endpoints that Sanjai can access after login.';

// Load the publisher endpoint list from the shared config.
export const PUBLISHERS_ENDPOINTS: EndpointDefinition[] =
  getEndpointsForModule(PUBLISHERS_MODULE_ID);
