# Product & Category Practice (Spring Boot + SQL Server)

## Features
- OneToMany: Category (1) -> Product (N)
- DTO: API không trả Entity trực tiếp
- Custom query filter theo:
  - categoryId (optional)
  - minPrice (optional)
  - maxPrice (optional)
- Pagination + Sort

## Tech
- Java 17+
- Spring Boot
- Spring Data JPA
- SQL Server

## Setup
1) Chạy file SQL: `sql/create_tables.sql` (tạo DB + tables)
2) Config `application.properties`
3) Run app

## APIs
### GET categories
GET `/api/categories`

### Create product
POST `/api/products`

Link video demo: https://youtu.be/xX-OrHm8DEA?si=Xgsxc4Pxg5ZsnBka

Update module: https://youtu.be/jIS3kmvHs5o?si=hvZaln9A6-19018Z
