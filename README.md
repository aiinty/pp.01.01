# ПП.01.01 Разработка мобильных приложений

Данный репозиторий содержит материалы и исходный код для прохождения практики по дисциплине "Разработка мобильных приложений". В рамках практики реализуется мобильное приложение Co.Payment.

[Макет в Figma](https://www.figma.com/design/D5nt9GJJVcvPWPrXEtvV9G/ПП.01.01?node-id=0-1&p=f&t=roT7U54mSHhrYn43-0).

## Бэкенд

Бэкенд для приложения реализован на платформе Supabase. `API_KEY` хранится в файле `local.properties`.

## Структура репозитория

- `docs/` — документация по проекту
- `app/` — исходный код мобильного приложения на Kotlin

## Используемые библиотеки

- [Navigation Compose](https://developer.android.com/develop/ui/compose/navigation)
- [DotsIndicator](https://github.com/tommybuonomo/dotsindicator)
- [Retrofit2](https://github.com/square/retrofit)
- [OkHttp Logging Interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor)
- [Gson](https://github.com/google/gson)
- [Converter Gson](https://github.com/square/retrofit/tree/trunk/retrofit-converters/gson)
- [Kotlinx Serialization JSON](github.com/Kotlin/kotlinx.serialization)
- [AndroidX Security Crypto](https://developer.android.com/jetpack/androidx/releases/security)
- [Dagger Hilt](https://dagger.dev/hilt)
- [Coil](https://github.com/coil-kt/coil)