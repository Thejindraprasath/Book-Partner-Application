// Defines one input field shown in the shared endpoint form.
export interface FormFieldDefinition {
  name: string;
  label: string;
  type: 'text' | 'number' | 'datetime-local';
  location: 'path' | 'query' | 'body';
  required?: boolean;
  defaultValue?: string | number;
  placeholder?: string;
}

// Describes one API action that can be shown and executed in the UI.
export interface EndpointDefinition {
  id: string;
  title: string;
  description: string;
  route: string;
  apiPath: string;
  method: 'GET' | 'POST' | 'PUT' | 'DELETE';
  formFields: FormFieldDefinition[];
}

// Describes one top-level module shown in the application.
export interface ModuleDefinition {
  id: string;
  label: string;
  description: string;
  loginHint: string;
  route: string;
  roles: string[];
  accent: string;
}
