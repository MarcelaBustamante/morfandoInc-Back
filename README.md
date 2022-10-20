# morfando-inc-back

To build the project run ./mvnw clean package

## JWT Auth Server keystore
```bash
keytool -genkey -alias jwtsigning -keyalg RSA -keystore keystore.jks -keysize 2048
```
Location:
`src/main/resources/keys/keystore.jks`