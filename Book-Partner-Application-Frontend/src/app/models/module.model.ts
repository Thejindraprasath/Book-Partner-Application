export interface FormFieldDefinition {
  name: string;
  label: string;
  type: 'text' | 'number' | 'datetime-local';
  location: 'path' | 'query' | 'body';
  required?: boolean;
  defaultValue?: string | number;
  placeholder?: string;
}

export interface EndpointDefinition {
  id: string;
  title: string;
  description: string;
  route: string;
  apiPath: string;
  method: 'GET' | 'POST' | 'PUT' | 'DELETE';
  formFields: FormFieldDefinition[];
}

export interface ModuleDefinition {
  id: string;
  label: string;
  description: string;
  loginHint: string;
  route: string;
  roles: string[];
  accent: string;
}
