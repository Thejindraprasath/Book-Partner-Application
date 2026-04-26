// Logged-in user details stored in the frontend session state.
export interface AuthUser {
  username: string;
  roles: string[];
  authenticated: boolean;
  moduleId: string;
}
