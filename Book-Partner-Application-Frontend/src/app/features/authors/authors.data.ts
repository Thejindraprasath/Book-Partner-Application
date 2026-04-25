export const AUTHORS_MODULE = {
  id: 'author',
  label: 'Authors Module',
  description: 'Manage author records including creating, viewing, updating, and deleting author details.',
  route: '/author',
  roles: ['ROLE_AUTHOR'],
};

export const AUTHORS_ROUTE = AUTHORS_MODULE.route;

export const AUTHORS_ENDPOINTS = [
  {
    id: 'get-all-authors',
    route: 'get-all',
    method: 'GET',
    title: 'View Authors',
    description: 'Fetch and display all available author records.',
    apiPath: '/api/v1/authors',
  },
  {
    id: 'create-author',
    route: 'create',
    method: 'POST',
    title: 'Create Author',
    description: 'Add a new author record with valid author details.',
    apiPath: '/api/v1/authors',
  },
  {
    id: 'update-author',
    route: 'update',
    method: 'PUT',
    title: 'Update Author',
    description: 'Modify existing author information using author ID.',
    apiPath: '/api/v1/authors/{authorId}',
  },
  {
    id: 'delete-author',
    route: 'delete',
    method: 'DELETE',
    title: 'Delete Author',
    description: 'Remove an author record using author ID.',
    apiPath: '/api/v1/authors/{authorId}',
  },
];
