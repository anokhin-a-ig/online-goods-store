# online-goods-store

## Разные профили для запуска приложения
* Test
* Dev
* Prod

  Необходимо зайти в настройки запуска проекта (edit configuration)

![img.png](images/edit-configuration.png)

и создать три разных профиля. В каждом из профилей указать в поле "Active profiles" суффикс соответствующего application-<суффикс>.yml файла для каждого профиля
![img.png](images/dev-profile.png)

В проекте реализованы файлы настроек application-<profile>.yml где profile - имя профиля. В каждом из файлов описаны настройки, активирующиеся при запуске соответствующего профиля.