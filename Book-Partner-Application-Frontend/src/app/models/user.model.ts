export interface AuthUser {
  username: string;
  roles: string[];
  authenticated: boolean;
  moduleId: string;
}
