
# Sample Spring Security + JWT  
This project is a demo **Spring Boot** application made with [Kotlin](https://kotlinlang.org). 
It shows how to use JWT based authentication in Spring Security.  
## Sample usage  
Authentication
```bash  
$ curl\  
> -X POST localhost:8080/auth/login\  
> -H 'Content-Type: application/json'\  
> -d '{"username":"user","password":"123"}'  
{"token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNTgxMjAxMDYzLCJleHAiOjE1ODMwMDYwNjY2NzEzNDV9.xQAHgIQGaKPRC4Ja-6uNiVtfV1OT0bO08Nj99DDW9k8","message":null}  
```
Authorization
```bash
$ curl\
> -X GET localhost:8080/users/me\ 
> -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNTgxMjAxMDYzLCJleHAiOjE1ODMwMDYwNjY2NzEzNDV9.xQAHgIQGaKPRC4Ja-6uNiVtfV1OT0bO08Nj99DDW9k8' | jq
{  
  "authorities": [],  
  "details": {  
    "remoteAddress": "0:0:0:0:0:0:0:1",  
    "sessionId": null  
  },  
  "authenticated": true,  
  "principal": {  
    "password": "123",  
    "username": "user",  
    "authorities": [],  
    "accountNonExpired": true,  
    "accountNonLocked": true,  
    "credentialsNonExpired": true,  
    "enabled": true  
  },  
  "credentials": null,  
  "name": "user" 
}
```