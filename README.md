# 🛒 T4 E-Commerce Spring Boot Application

## 📌 Project Overview
McDiffyStore is a vendor that sells products across multiple categories. This project is a Spring Boot RESTful backend application developed as an initial MVP for a distributed e-commerce platform. The application supports JWT-based authentication, role-based authorization (CONSUMER & SELLER), and secure APIs for product management and cart operations.

## 🛠️ Tech Stack
Java, Spring Boot, Spring Security, JWT Authentication, JPA/Hibernate, H2/MySQL (as configured), Maven

## 🔐 Authentication & Authorization
All authentication and authorization is implemented using JWT Tokens. The JWT token must be passed as a Bearer token in the Authorization header:  
Authorization: Bearer <JWT_TOKEN>  
If authenticated endpoints are accessed without JWT → 401 Unauthorized  
If a CONSUMER endpoint is accessed using a SELLER JWT or vice versa → 403 Forbidden

## 👥 Roles
CONSUMER, SELLER

## 🗄️ Database Initialization

### Categories
1. Fashion  
2. Electronics  
3. Books  
4. Groceries  
5. Medicines  

### Roles
CONSUMER, SELLER

### Users
1. jack | pass_word | CONSUMER  
2. bob | pass_word | CONSUMER  
3. apple | pass_word | SELLER  
4. glaxo | pass_word | SELLER  
(Passwords are stored in encrypted form)

### Cart
User 1 → totalAmount = 20  
User 2 → totalAmount = 0  

### Products
- 29190 | Apple iPad 10.2 8th Gen WiFi iOS Tablet | categoryId 2 | sellerId 3  
- 10 | Crocin pain relief tablet | categoryId 5 | sellerId 4  

### CartProduct
cartId 1 | productId 2 | quantity 2

## 🌐 API Endpoints

### 🔓 Public APIs

GET /api/public/product/search?keyword=tablet  
Searches products by productName or categoryName  
Success → 200  
Error → 400  

POST /api/public/login  
Request Body:  
{
  "username": "jack",
  "password": "pass_word"
}  
Returns JWT token  
Success → 200  
Invalid credentials → 401  

## 🧑‍💼 Consumer APIs (Authenticated)

GET /api/auth/consumer/cart  
Returns the logged-in consumer’s cart  

POST /api/auth/consumer/cart  
Adds product to cart  
Request Body:  
{
  "productId": 3,
  "category": {
    "categoryId": 2,
    "categoryName": "Electronics"
  },
  "price": 98000.0,
  "productName": "iPhone 12"
}  
Success → 201  
If product already exists in cart → 409  

PUT /api/auth/consumer/cart  
Updates product quantity in cart  
If product not present → add to cart  
If quantity = 0 → remove product  
Request Body:  
{
  "product": {
    "productId": 3,
    "category": {
      "categoryId": 2,
      "categoryName": "Electronics"
    },
    "price": 98000.0,
    "productName": "iPhone 12"
  },
  "quantity": 3
}  
Success → 200  

DELETE /api/auth/consumer/cart  
Removes product from cart  
Request Body:  
{
  "productId": 3,
  "category": {
    "categoryId": 2,
    "categoryName": "Electronics"
  },
  "price": 98000.0,
  "productName": "iPhone 12"
}  
Success → 200  

## 🏪 Seller APIs (Authenticated)

GET /api/auth/seller/product/{productId}  
Returns product owned by seller for given productId  

POST /api/auth/seller/product  
Adds a new product  
Request Body:  
{
  "productId": 3,
  "category": {
    "categoryId": 2,
    "categoryName": "Electronics"
  },
  "price": 98000.0,
  "productName": "iPhone 12 Pro Max"
}  
Success → 201  
Redirect URI → /api/auth/seller/product/3  

GET /api/auth/seller/product  
Returns all products owned by the seller  

PUT /api/auth/seller/product  
Updates an existing product (productId mandatory)  
Request Body:  
{
  "productId": 3,
  "category": {
    "categoryId": 2,
    "categoryName": "Electronics"
  },
  "price": 98000.0,
  "productName": "iPhone 12 Pro Max"
}  
Success → 200  

DELETE /api/auth/seller/product/{productId}  
Deletes product owned by seller  
Success → 200  
If product not owned by seller → 404  

## ⚠️ Error Handling
400 → Bad Request  
401 → Unauthorized (missing/invalid JWT)  
403 → Forbidden (wrong role access)  
404 → Resource not found  

## 🚀 Getting Started
1. Clone the repository  
2. Configure application properties  
3. Run the Spring Boot application  
4. Test APIs using Postman  

## ✅ Notes
Follow the test cases strictly for validations. JWT authentication is mandatory for all protected APIs.

### 🎯 Good Luck & Happy Coding!
