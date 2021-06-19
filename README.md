# Seguridad

La idea es agregar una columna extra al `ContentProvider` de la aplicación de *ApkLis* que funcionaría similar a un *MD5* pero con un método de encriptación asimétrica *RSA* donde la clave privada se usaría solamente en el `ContentProvider` mientras la clave pública se utiliza en la librería `apklischeckpayment` para verificar que el `ContentProvider` que brinda la informacón es legítimo. De esta forma se disminuye la posibilidad de suplantación de identidad con el `ContentProvider`
  
En resumen el mecanismo funciona similar a una firma digital.

La inviolabilidad del mismo depende de cuan bien resguardad se encuentre la clave privada que utilizará el `ContentProvider`



# Check app paid in [Apklis](https://www.apklis.cu/es/) APK
[![](https://jitpack.io/v/Z17-CU/apklischeckpayment.svg)](https://jitpack.io/#Z17-CU/apklischeckpayment)

### Installing
* Step 1. Add the JitPack repository to your build file
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
* Step 2. Add the dependency
```groovy

   implementation 'com.github.Z17-CU:apklischeckpayment:$VERSION'
```
* Step 2.1 For Java App Add this dependency
```groovy

   implementation 'org.jetbrains.kotlin:kotlin-stdlib-jdk8:$LAST_VERSION'
```

### Usage

* It is necessary to have the Apklis application installed and a section started to check, otherwise the user would be null and the payment would be false.

* Check paid Kotlin
```kotlin
    val response = Verify.isPurchased(this, PACKAGE_ID)
    val userName = response.second
    val isPaid = response.first
```

* Check paid Java
```java
   Pair<Boolean, String> response = Verify.Companion.isPurchased(this, PACKAGE_ID);
   String userName = response.getSecond();
   Boolean isPaid = response.getFirst();
```
### Contributing
All contributions are welcome!!!
