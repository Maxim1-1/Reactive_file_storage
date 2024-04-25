Данное приложение предоставляет аналог Google Drive, т.е. API для удаленного хранения файлов пользователей в хранилище S3, с возможностью получения истории загрузки.   

#### В проекте предоставлены три уровня доступа:  
- USER - имеет доступ только к своим файлам  
- ADMIN - имеет полный доступ к приложению   
- MODERATOR - имеет полный доступ к приложению, за исключением функции удалением USER  

#### Запуска приложения локально
1. Проверьте, что Docker установлен на вашем устройстве
2. Заполните следующие параметры в файле **docker-compose.yaml**:
   
      - R2DBC_URL=r2dbc:mysql://mysql_db/**{your_db_name}**
      - R2DBC_USER=**{your_db_user}**
      - R2DBC_PASSWORD=**{your_db_password}**
      - DB_URL=jdbc:mysql://mysql_db:3306/**{your_db_name}**
      - DB_USER=**{your_db_user}**
      - DB_PASSWORD=**{your_db_password}**
      - S3_ACCESS_KEY=**ACCESS KEY FOR S3**
      - S3_SECRET_KEY=**SECRET KEY FOR S3**
      - S3_REGION=**YOUR_REGION**
      - S3_URL=**YOUR_URL**
      - S3_BUCKET=**YOUR BUCKET NAME**
3. Выполните команду `docker-compose up` для запуска приложения
      
#### Стек технологий: 
* Spring WebFlux
* Security, Boot
* R2DBC
* Amazon S3
* Flyway
* MySQL  
