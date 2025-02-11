import { AuthConfig } from 'angular-oauth2-oidc';

export const authCodeFlowConfig: AuthConfig = {
  issuer: 'http://localhost:8080/realms/freddy',
  redirectUri: `${window.location.origin}`,
  clientId: 'shop-frontend',
 // responseType: 'code',
  scope: 'openid profile email offline_access',
  showDebugInformation: true,
  timeoutFactor: 0.01,
  strictDiscoveryDocumentValidation: false,
  skipIssuerCheck: true,
  requireHttps: false,
};
