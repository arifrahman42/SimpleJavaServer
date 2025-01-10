# Simple Java Server

Simple Java Server adalah proyek yang saya kembangkan sebagai latihan untuk membangun program aplikasi peladen sederhana menggunakan bahasa pemrograman Java.

Saat aplikasi dijalankan, maka aplikasi ini akan menampilkan nama, waktu saat ini, persentase tahun, serta informasi suhu udara melalui antarmuka web.

```java
Arif Rahman Habibie
Jumat, 29 November 2024 07:03:15
90% menuju tahun 2025
Suhu udara di Medan saat ini adalah 23.4°C. Terakhir dimutakhirkan pada pukul 07:00 WIB
```

Program ini saya kembangkan menggunakan [Java JDK versi 23](https://docs.aws.amazon.com/corretto/latest/corretto-23-ug/what-is-corretto-23.html) beserta [IDE Visual Studio Code](https://code.visualstudio.com/).

Program ini saya perkirakan dapat di-compile dan dijalankan dengan minimum [Java JDK versi 11 LTS](https://www.oracle.com/java/technologies/javase/11-relnote-issues.html), namun saya sarankan (per Januari 2025 ini) untuk mulai menggunakan [Java JDK versi 21 LTS](https://www.oracle.com/java/technologies/javase/21-relnote-issues.html) atau yang terbaru [versi 23](https://www.oracle.com/java/technologies/javase/23-relnote-issues.html).

Informasi suhu udara bersumber dari [API Open Meteo](https://api.open-meteo.com/v1/forecast?latitude=3.5833&longitude=98.6667&current=temperature_2m) (Lokasi: Medan, Sumatera Utara, Indonesia; Parameter: Suhu Udara)

Lokasi serta parameter lainnya dapat disesuaikan melalui antarmuka pengguna [berikut](https://open-meteo.com/en/docs).

#