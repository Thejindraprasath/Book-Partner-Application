import { APP_MODULES, getEndpointsForModule } from '../../config/api.config';

// Read the author module details from the central config.
export const AUTHORS_MODULE = APP_MODULES.find(
  (moduleItem) => moduleItem.id === 'author'
)!;

// Reuse the same route and endpoint list everywhere in the author feature.
export const AUTHORS_ROUTE = AUTHORS_MODULE.route;

export const AUTHORS_ENDPOINTS = getEndpointsForModule('author');
