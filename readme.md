<h1 align="center"> Redis-example  </h1> <br>  

## Table of Contents

- [Introduction](#introduction)
- [How to build & How to run](#how-to-build--how-to-run)
- [API](#api)

## Introduction

This repo purposes to show common cases and configurations about Redis & Spring boot. U can find some examples and
generic
structure to work with redisTemplate

## How to build & How to run

1. Clone the repo
   ```sh
   git clone https://github.com/Rufet2020/redis-example.git
   ```

2. Run docker-compose.yaml file located in project structure
   ```sh
   U need to docker installed to ur machine
   ```

3. Open project in your favorite IDEA and run. If you wish you can build and run project via terminal

   3.1. Build project
    ```sh
    $ ./gradlew build
    ```
   3.2. Run project
    ```sh
    $ java -jar app/build/libs/redis-example.jar
    ```

There are used PostgreSql, Redis, by default project running on localhost:8080

# redis-example endpoints

<details>
<summary>Enpoints by controller</summary>

# Order endpoints

| Endpoint                                                  |   Method   | Description                                             |  
|:----------------------------------------------------------|:----------:|:--------------------------------------------------------|
| localhost:8080/redis-example/v1/order                     |  **POST**  | Create order and cache with annotation                  |
| localhost:8080/redis-example/v2/order                     |  **POST**  | Create order and cache on manual config                 |
| localhost:8080/redis-example/v1/order/:id                 |  **GET**   | Get order by id and cache with annotation               |
| localhost:8080/redis-example/v2/order/:id                 |  **GET**   | Get order by id and cache on manual config              |
| localhost:8080/redis-example/v1/order                     |  **GET**   | Get all orders and cache with annotation                |
| localhost:8080/redis-example/v2/order                     |  **GET**   | Get all orders and cache on manual config               |
| localhost:8080/redis-example/v2/order/search/:query       |  **GET**   | Search order by any query without any caching process   |
| localhost:8080/redis-example/v2/order/search-cache/:query |  **GET**   | Search order by any query with caching result           |
| localhost:8080/redis-example/v1/order/brand/:brand        |  **GET**   | Get order by brand name and use brand name as cache key |
| localhost:8080/redis-example/v1/order/brand/:brand/:name  |  **GET**   | Get order by brand name & name and use both as key      |
| localhost:8080/redis-example/v1/order/:id                 | **DELETE** | Delete order by id and cache evict with annotation      |
| localhost:8080/redis-example/v2/order/:id                 | **DELETE** | Delete order by id and cache evict with manual config   |

</details>
