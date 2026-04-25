import { APP_MODULES, getEndpointsForModule } from '../../config/api.config';

export const AUTHORS_MODULE = APP_MODULES.find(
  (moduleItem) => moduleItem.id === 'author'
)!;

export const AUTHORS_ROUTE = AUTHORS_MODULE.route;

export const AUTHORS_ENDPOINTS = getEndpointsForModule('author');
