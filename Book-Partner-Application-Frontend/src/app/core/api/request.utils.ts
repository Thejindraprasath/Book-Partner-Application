// Pick only query-string values that actually have content.
export function toQueryParams(values: Record<string, unknown>, keys: string[]): Record<string, string | number | boolean> {
  const params: Record<string, string | number | boolean> = {};

  for (const key of keys) {
    const value = values[key];
    if (value !== undefined && value !== null && value !== '') {
      params[key] = value as string | number | boolean;
    }
  }

  return params;
}

// Pick only body fields that actually have content.
export function pickBody(values: Record<string, unknown>, keys: string[]): Record<string, unknown> {
  return Object.fromEntries(
    keys
      .filter((key) => values[key] !== undefined && values[key] !== null && values[key] !== '')
      .map((key) => [key, values[key]])
  );
}
