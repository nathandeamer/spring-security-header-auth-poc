# Spring Security Header POC

Proof of concept showing how API endpoints can require authorization using customer headers (sent from an API G/W managing auth)

## How it works?
The [ScopesAuthenticationFilter](src/main/java/com/nathandeamer/security/demo/ScopesAuthenticationFilter.java) requires 2 headers to be passed from an API G/W
- `x-my-custom-principal` - a unique identifier of the user.
- `x-my-custom-scopes` - a comma separated list of scopes that the user has.

The scopes are then converted into authorities that can be used to protect the endpoint

[![](https://mermaid.ink/img/pako:eNp1kU9LAzEQxb_KkIModuk9h0KhIj0UxPrnksuQTN3gJlmTibaUfnc3pi5U25zC473fzPD2QgdDQopEH5m8poXFt4hOeRjec6I4XaFurSdoZrNbmGduQ7QJ2QYPa4qfFOWoUo2dM5V4cwKU8BTeySt_cdbDEu6nrxIey26Jj_Cq_rEsqNwBXImXfXdbjqgZkg49JXXOW_a1mmA-DobrL8sttISGYoJt43aNzomDa_povbY9dnB1olf-TcWPxH8DXrCzBpl-2WIiHEWH1gyV7EtaCW7JkRJy-BraYO5YCeUPgxUzh_XOayE5ZpqI3BfWsUEhN9ilQSVjOcRVrfmn7cM3zNOiqg?type=png)](https://mermaid.live/edit#pako:eNp1kU9LAzEQxb_KkIModuk9h0KhIj0UxPrnksuQTN3gJlmTibaUfnc3pi5U25zC473fzPD2QgdDQopEH5m8poXFt4hOeRjec6I4XaFurSdoZrNbmGduQ7QJ2QYPa4qfFOWoUo2dM5V4cwKU8BTeySt_cdbDEu6nrxIey26Jj_Cq_rEsqNwBXImXfXdbjqgZkg49JXXOW_a1mmA-DobrL8sttISGYoJt43aNzomDa_povbY9dnB1olf-TcWPxH8DXrCzBpl-2WIiHEWH1gyV7EtaCW7JkRJy-BraYO5YCeUPgxUzh_XOayE5ZpqI3BfWsUEhN9ilQSVjOcRVrfmn7cM3zNOiqg)

## How to add authentication to a method:
Update the [SecurityConfig](src/main/java/com/nathandeamer/security/demo/SecurityConfig.java) with the route and the required authorisation

Examples:   
`.requestMatchers(HttpMethod.GET, "/noauth").permitAll()`
`.requestMatchers(HttpMethod.GET,"/auth").hasAuthority("customer:read")`

### Best practices: 
- Deny all by default
- Avoid using wildcards. 
- A new request matcher per HTTP endpoint

### Spring Security Filter Chain:
When a HTTP request is received, it will go through all of these filters in order:
```
org.springframework.security.web.session.DisableEncodeUrlFilter
org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter
org.springframework.security.web.context.SecurityContextHolderFilter
org.springframework.security.web.header.HeaderWriterFilter
org.springframework.security.web.csrf.CsrfFilter
org.springframework.security.web.authentication.logout.LogoutFilter
org.springframework.security.web.authentication.www.BasicAuthenticationFilter
org.springframework.security.web.savedrequest.RequestCacheAwareFilter
org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter
org.springframework.security.web.authentication.AnonymousAuthenticationFilter
org.springframework.security.web.access.ExceptionTranslationFilter
org.springframework.security.web.access.intercept.AuthorizationFilter
```

I have inserted the custom `ScopesAuthenticationFilter` before `BasicAuthenticationFilter` (the first Authentication Filter)  
See [SecurityConfig.java](src/main/java/com/nathandeamer/security/demo/SecurityConfig.java)