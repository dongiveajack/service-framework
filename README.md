# Service Framework — Comprehensive Technical Reference

> A production-grade, opinionated Spring Boot foundation framework for building multitenant microservices with zero boilerplate.

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack & Dependencies](#tech-stack--dependencies)
- [Getting Started](#getting-started)
- [Architecture Overview](#architecture-overview)
- [Package Structure](#package-structure)
- [Core Components](#core-components)
  - [BaseEntity](#baseentity)
  - [BaseRepository](#baserepository)
  - [BaseService](#baseservice)
  - [BaseController](#basecontroller)
  - [BaseResponse & StatusResponse](#baseresponse--statusresponse)
  - [BaseMapper](#basemapper)
  - [IdGenerator](#idgenerator)
- [Search Engine](#search-engine)
  - [SearchHelper](#searchhelper)
  - [SearchOperator Enum](#searchoperator-enum)
  - [CustomSearchSpecification](#customsearchspecification)
  - [SearchEntry](#searchentry)
  - [Filter Syntax Reference](#filter-syntax-reference)
  - [JSONB Query Support](#jsonb-query-support)
  - [Sorting & Pagination](#sorting--pagination)
  - [Nested Entity Joins](#nested-entity-joins)
- [AOP Security & Context](#aop-security--context)
  - [@Authenticate Annotation](#authenticate-annotation)
  - [AuthenticateAspect](#authenticateaspect)
  - [Context (ThreadLocal)](#context-threadlocal)
  - [@PopulateUserData & UserDataAspect](#populateuserdata--userdataaspect)
  - [@UserField Annotation](#userfield-annotation)
  - [@Client Annotation](#client-annotation)
  - [@FeignClientAdvice Annotation](#feignclientadvice-annotation)
- [Multitenancy](#multitenancy)
  - [RoutingDataSource](#routingdatasource)
  - [DataSourcePropertiesConfig & FaasDataSourceProperties](#datasourcepropertiesconfig--faasdatasourceproperties)
  - [Flyway Migrations](#flyway-migrations)
- [Authentication Service](#authentication-service)
- [Microservice Clients](#microservice-clients)
  - [RealmClient](#realmclient)
  - [MercuryClient](#mercuryclient)
  - [Client DTOs & Responses](#client-dtos--responses)
- [Notification System](#notification-system)
  - [NotificationService](#notificationservice)
  - [NotificationHandler Interface & Factory](#notificationhandler-interface--factory)
  - [SMS / Email / WhatsApp Handlers](#sms--email--whatsapp-handlers)
- [Content Management System (CMS)](#content-management-system-cms)
  - [CmsService](#cmsservice)
  - [CmsController](#cmscontroller)
  - [GraphQL Integration](#graphql-integration)
  - [SNS Event Publishing](#sns-event-publishing)
- [Audit System (JaVers)](#audit-system-javers)
  - [AuditLogController](#auditlogcontroller)
  - [AuditLogService](#auditlogservice)
  - [JaVers Configuration](#javers-configuration)
  - [Custom Type Adapters](#custom-type-adapters)
- [Exception Handling](#exception-handling)
  - [RestExceptionHandler](#restexceptionhandler)
  - [gRPC Exception Handling](#grpc-exception-handling)
  - [Feign Client Exception Handling](#feign-client-exception-handling)
  - [Custom Exception Classes](#custom-exception-classes)
- [Auto-Configuration](#auto-configuration)
  - [RestConfig (ObjectMapper)](#restconfig-objectmapper)
  - [SwaggerConfig (OpenAPI 3.0)](#swaggerconfig-openapi-30)
  - [GrpcConfig](#grpcconfig)
  - [CacheConfig (EhCache)](#cacheconfig-ehcache)
  - [FeignClientConfig](#feignclientconfig)
  - [SnsConfig (AWS SNS)](#snsconfig-aws-sns)
  - [ApplicationConfig](#applicationconfig)
  - [RabbitConfig](#rabbitconfig)
- [Utility Classes](#utility-classes)
  - [HttpUtils & CookieUtils](#httputils--cookieutils)
  - [ReflectionUtils](#reflectionutils)
  - [StringUtils](#stringutils)
  - [ValidationUtils](#validationutils)
  - [GraphQLUtils & CmsUtils](#graphqlutils--cmsutils)
  - [ProtoMapperUtil](#protomapperutil)
  - [UomUtils & UnitOfMeasurement](#uomutils--unitofmeasurement)
  - [HttpRequestInterceptor & SqlStatementInterceptor](#httprequestinterceptor--sqlstatementinterceptor)
- [Status Codes](#status-codes)
- [Configuration Properties Reference](#configuration-properties-reference)
- [Build & Publish](#build--publish)

---

## Overview

**Service Framework** is a Java 11 library built on Spring Boot 2.7.1 that eliminates the repetitive setup required when building microservices. Instead of spending days configuring databases, pagination, session management, CRUD APIs, and error handling, you extend a few base classes and get a production-ready microservice instantly.

The framework is published via **JitPack** and designed to be included as a Gradle dependency in downstream microservices.

### Key Capabilities

| Capability | Description |
|---|---|
| **Zero-Boilerplate CRUD** | Extend `BaseController` + `BaseService` → get 7 REST endpoints automatically |
| **Dynamic Search Engine** | Frontend-driven query DSL with 15 operators, JSONB support, sorting, pagination |
| **AOP Security** | Annotation-based authentication with cookie/client-id auth strategies |
| **Multitenancy** | Namespace-based database routing with per-tenant Hikari connection pools |
| **Audit Trail** | JaVers-powered entity versioning with built-in audit log API |
| **Notifications** | Factory-pattern SMS, Email, and WhatsApp via Mercury client |
| **CMS Integration** | GraphQL-based SKU management with find-or-create semantics |
| **gRPC Support** | Pre-configured gRPC server interceptors and exception handlers |
| **AWS Integration** | SNS event publishing for domain events |
| **Standardized Errors** | Consistent JSON error responses across REST and gRPC |

---

## Tech Stack & Dependencies

| Category | Technology | Version |
|---|---|---|
| **Language** | Java | 11 |
| **Framework** | Spring Boot | 2.7.1 |
| **Build Tool** | Gradle | Wrapper included |
| **ORM** | Spring Data JPA + Hibernate 5 | — |
| **Query DSL** | QueryDSL JPA | 5.0.0 |
| **Database Migration** | Flyway | 9.16.0 |
| **Caching** | EhCache + Spring Data Redis | 2.10.9.2 |
| **Serialization** | Jackson (Hibernate5, Joda, JSR310, JsonOrg modules) | 2.13.3 |
| **REST Clients** | Spring Cloud OpenFeign | 3.1.5 |
| **gRPC** | grpc-spring-boot-starter | 4.7.0 |
| **API Docs** | Springfox (OpenAPI 3.0 / Swagger) | 3.0.0 |
| **Audit** | JaVers | 6.6.5 |
| **Mapping** | MapStruct | 1.5.3 |
| **AWS** | SNS, S3 (spring-cloud-aws) | 2.2.6 |
| **GraphQL** | graphql-java-kickstart WebClient | 1.0.0 |
| **Validation** | Hibernate Validator + javax.validation | 6.2.0 / 2.0.1 |
| **Utilities** | Lombok, Guava, Apache Commons (Lang3, Text), Gson | Various |
| **Connection Pool** | HikariCP (via Spring Boot) | — |
| **Hibernate Types** | vladmihalcea hibernate-types-52 | 2.21.1 |
| **Date/Time** | Joda-Time + Jadira UserType | 7.0.0.CR1 |
| **Code Generation** | JavaPoet, Google Auto-Service | 1.13.0 / 1.1.1 |
| **Publishing** | JitPack | OpenJDK 11 |

---

## Getting Started

### Prerequisites

- **Java 11** (OpenJDK or equivalent)
- **Gradle** (wrapper included in the project)

### Installation

Add the framework as a dependency in your microservice's `build.gradle`:

```gradle
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.ironman19933:service-framework:4.0.1'
}
```

### Minimal Usage Example

```java
// 1. Define your entity
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    private String name;
    private Double price;
    private String category;
}

// 2. Define your repository
public interface ProductRepository extends BaseRepository<Product> {}

// 3. Define your service
@Service
public class ProductService extends BaseService<Product> {
    public ProductService(ProductRepository repository) {
        super(repository, Product.class);
    }

    @Override
    protected Product merge(Product source, Product target) {
        target.setName(source.getName());
        target.setPrice(source.getPrice());
        target.setCategory(source.getCategory());
        return target;
    }
}

// 4. Define your response
@Data
@SuperBuilder
public class ProductResponse extends BaseResponse {
    private List<Product> data;
}

// 5. Define your controller
@RestController
@RequestMapping("/products")
public class ProductController extends BaseController<ProductResponse, Product> {
    public ProductController(ProductService service) {
        super(service);
    }

    @Override
    protected ProductResponse createResponse(List<Product> entryList) {
        return ProductResponse.builder().data(entryList).build();
    }
}
```

**Result**: You now have 7 fully authenticated, paginated REST endpoints with dynamic search, soft-delete, and audit logging — zero extra code.

---

## Architecture Overview

```
┌────────────────────────────────────────────────────────────┐
│                     HTTP / gRPC Request                     │
├────────────────────────────────────────────────────────────┤
│         ApplicationConfig (HttpRequestInterceptor)          │
├────────────────────────────────────────────────────────────┤
│            AuthenticateAspect (AOP @Around)                 │
│   ┌──────────────────────────────────────────────────┐     │
│   │  Namespace Validation → Auth (Cookie/ClientID)   │     │
│   │  → Context.setUserId() + Context.setNamespaceId()│     │
│   └──────────────────────────────────────────────────┘     │
├────────────────────────────────────────────────────────────┤
│                    BaseController                           │
│   GET /{id} | POST / | PUT /{id} | DELETE /{id}           │
│   PUT /bulk | GET /search | POST /search                  │
├────────────────────────────────────────────────────────────┤
│                     BaseService                             │
│   find | save | update | delete | bulkCreateOrUpdate       │
│   search (filter parsing → specification → pagination)     │
├────────────────────────────────────────────────────────────┤
│                  Search Engine Layer                        │
│   SearchHelper → CustomSearchSpecification → Predicates    │
├────────────────────────────────────────────────────────────┤
│                   BaseRepository                            │
│   JpaRepository + JpaSpecificationExecutor + QueryDSL      │
├────────────────────────────────────────────────────────────┤
│                 RoutingDataSource                           │
│   Context.getNamespaceId() → Tenant-specific HikariPool    │
├────────────────────────────────────────────────────────────┤
│              PostgreSQL (per-tenant databases)              │
└────────────────────────────────────────────────────────────┘
```

---

## Package Structure

```
org.trips.service_framework
├── annotations/           # @AuditAdapter annotation + compiler processor
│   ├── AuditAdapter.java
│   └── AuditAdapterProcessor.java
├── aop/                   # Annotations and AOP aspects
│   ├── Authenticate.java         # Method-level auth annotation
│   ├── Client.java               # Component stereotype for clients
│   ├── FeignClientAdvice.java     # Feign error handler binding
│   ├── PopulateUserData.java      # Auto-populate user data annotation
│   ├── UserField.java             # Field-level user data marker
│   └── aspects/
│       ├── AuthenticateAspect.java   # Authentication AOP logic
│       └── UserDataAspect.java       # User data population AOP logic
├── audit/                 # JaVers audit subsystem
│   ├── JaversAuthorProvider.java     # Binds Context.getUserId() to JaVers
│   ├── JaversConnectionProvider.java # Custom JDBC connection for JaVers
│   ├── JaversDatasourceConfig.java   # Separate audit routing datasource
│   ├── adapters/                     # JaVers type adapters
│   │   ├── DateTimeAdapter.java
│   │   ├── JSONObjectAdapter.java
│   │   └── JsonNodeAdapter.java
│   ├── controllers/
│   │   └── AuditLogController.java   # POST /audit/changes endpoint
│   ├── dtos/
│   │   ├── AuditEntry.java
│   │   └── ChangeDetail.java
│   ├── requests/
│   │   └── AuditRequest.java
│   ├── responses/
│   │   └── AuditResponse.java
│   └── services/
│       └── AuditLogService.java      # JaVers query + user resolution
├── clients/               # Feign clients for sibling microservices
│   ├── MercuryClient.java            # Notification service client
│   ├── MercuryClientInterceptor.java # Forwards namespace header
│   ├── RealmClient.java              # Auth/user service client
│   ├── request/
│   │   ├── NotificationRequest.java
│   │   └── RealmUserSearchRequest.java
│   └── response/
│       ├── NotificationResponse.java
│       ├── RealmClientsVerifyResponse.java
│       ├── RealmSessionInfoResponse.java
│       ├── RealmUser.java
│       ├── RealmUserResponse.java
│       └── ResponseStatus.java
├── codes/                 # Standardized status code enums
│   ├── ErrorCodes.java
│   ├── StatusCode.java
│   └── SuccessCodes.java
├── configs/               # Auto-configuration classes
│   ├── ApplicationConfig.java        # HTTP interceptor registration
│   ├── CacheConfig.java              # EhCache setup
│   ├── DataSourceConfig.java         # Primary routing datasource bean
│   ├── DataSourcePropertiesConfig.java # YAML-bound datasource properties
│   ├── FaasDataSourceProperties.java  # Per-tenant datasource config
│   ├── FeignClientConfig.java         # Feign request interceptor
│   ├── GrpcConfig.java               # gRPC global interceptor
│   ├── RabbitConfig.java             # RabbitMQ (placeholder)
│   ├── RestConfig.java               # ObjectMapper configuration
│   ├── SnsConfig.java                # AWS SNS client
│   └── SwaggerConfig.java           # OpenAPI 3.0 Docket
├── constants/             # Domain constants
│   └── CmsConstants.java
├── controllers/           # Base and domain controllers
│   ├── BaseController.java
│   └── CmsController.java
├── dtos/                  # Data Transfer Objects
│   ├── CmsSearchRequestBody.java
│   ├── CmsSkuResponse.java
│   ├── EmailNotificationRequest.java
│   ├── SMSNotificationRequest.java
│   ├── SkuAttributes.java
│   └── WhatsappNotificationRequest.java
├── enums/
│   └── UnitOfMeasurement.java
├── events/                # Domain event publishing
│   ├── SnsEventPublisher.java
│   └── dto/
│       └── SkuCreationTopicMessage.java
├── exceptions/            # Custom exceptions + handlers
│   ├── AccessDeniedException.java
│   ├── CacheNotFoundException.java
│   ├── CmsException.java
│   ├── CustomErrorDecoder.java
│   ├── FeignBaseException.java
│   ├── GeneralException.java
│   ├── InternalAuthenticationServiceException.java
│   ├── NotAllowedException.java
│   ├── NotFoundException.java
│   ├── RealmException.java
│   ├── ServiceException.java
│   ├── ValidationException.java
│   ├── annotations/
│   │   ├── FeignClientExceptionHandler.java
│   │   └── GrpcExceptionHandler.java
│   ├── handlers/
│   │   ├── BaseGrpcExceptionHandler.java
│   │   ├── GrpcServiceExceptionHandler.java
│   │   ├── RestExceptionHandler.java
│   │   └── feignClientExceptionHandlers/
│   │       └── RealmExceptionHandler.java
│   └── interceptor/
│       └── GrpcExceptionInterceptor.java
├── helpers/
│   └── CmsHelper.java
├── interceptors/
│   └── SqlStatementInterceptor.java
├── mappers/
│   └── BaseMapper.java
├── models/                # Core domain models
│   ├── CustomSearchSpecification.java
│   ├── IdGenerator.java
│   ├── SearchOperator.java
│   ├── SearchSpecification.java
│   ├── daos/
│   │   └── BaseDao.java
│   ├── entities/
│   │   └── BaseEntity.java
│   ├── entries/
│   │   ├── BaseEntry.java
│   │   └── SearchEntry.java
│   └── responses/
│       ├── BaseResponse.java
│       └── StatusResponse.java
├── notificationHandler/   # Notification dispatch subsystem
│   ├── EmailNotificationHandler.java
│   ├── NotificationHandler.java      # Interface
│   ├── NotificationHandlerFactory.java
│   ├── NotificationType.java         # SMS, EMAIL, WHATSAPP
│   ├── SMSNotificationHandler.java
│   ├── Strings.java
│   ├── WhatsappNotificationHandler.java
│   └── dtos/
│       ├── EmailDto.java
│       ├── SMSDto.java
│       └── WhatsappDto.java
├── responses/
│   └── CmsResponse.java
├── services/              # Core service layer
│   ├── AuthService.java
│   ├── BaseService.java
│   ├── CmsService.java
│   └── NotificationService.java
└── utils/                 # Utility classes
    ├── CmsUtils.java
    ├── Constants.java
    ├── Context.java
    ├── CookieUtils.java
    ├── GraphQLUtils.java
    ├── HttpRequestInterceptor.java
    ├── HttpUtils.java
    ├── ProtoMapperUtil.java
    ├── ReflectionUtils.java
    ├── RoutingDataSource.java
    ├── SearchHelper.java
    ├── StringUtils.java
    ├── UomUtils.java
    └── ValidationUtils.java
```

**Total: 63 Java source files across 20+ packages.**

---

## Core Components

### BaseEntity

**File**: `models/entities/BaseEntity.java`

All domain entities must extend `BaseEntity`. It is a `@MappedSuperclass` providing the following columns automatically:

| Field | Column | Type | Description |
|---|---|---|---|
| `id` | `id` | `Long` | Primary key, auto-generated via `IdGenerator` |
| `createdBy` | `created_by` | `String` | Set from `Context.getUserId()` on `@PrePersist` |
| `updatedBy` | `updated_by` | `String` | Set from `Context.getUserId()` on `@PreUpdate` |
| `createdAt` | `created_at` | `DateTime` (Joda) | Set to `DateTime.now()` on `@PrePersist` |
| `updatedAt` | `updated_at` | `DateTime` (Joda) | Updated to `DateTime.now()` on `@PreUpdate` |
| `version` | `version` | `Long` | Optimistic locking via `@Version` (default: `0`) |
| `deletedAt` | `deleted_at` | `DateTime` (Joda) | Non-null = soft-deleted |
| `deletedBy` | `deleted_by` | `String` | Who performed the soft delete |
| `namespaceId` | `namespace_id` | `String` | Tenant ID, set from `Context.getNamespaceId()` |

**Lifecycle Hooks:**

- `@PrePersist (onCreate)`: Auto-fills `createdAt`, `updatedAt`, `createdBy`, `updatedBy`, and `namespaceId`. Falls back to `"System"` if no user context exists.
- `@PreUpdate (onUpdate)`: Refreshes `updatedAt` and `updatedBy`.

**Annotations used:** `@Data`, `@SuperBuilder`, `@MappedSuperclass`, `@AllArgsConstructor`, `@NoArgsConstructor`.

---

### BaseRepository

**File**: `models/repositories/BaseRepository.java`

```java
public interface BaseRepository<M extends BaseEntity>
    extends JpaRepository<M, Long>,
            JpaSpecificationExecutor<M>,
            QuerydslPredicateExecutor<M> {}
```

A triple-extended repository interface giving your entity:
- Standard CRUD operations (`JpaRepository`)
- Specification-based dynamic queries (`JpaSpecificationExecutor`) — used by the search engine
- QueryDSL type-safe queries (`QuerydslPredicateExecutor`)

Simply extend this interface with your entity type, and you get all three capabilities.

---

### BaseService

**File**: `services/BaseService.java`

An abstract service class providing transactional business logic for any entity.

| Method | HTTP Verb Equivalent | Transaction | Description |
|---|---|---|---|
| `find(Long id)` | GET | Read-only | Finds entity by ID, returns `null` if not found |
| `save(Entity)` | POST | Read-write | Persists a new entity |
| `update(Entity, Long id)` | PUT | Read-write | Loads existing entity by ID, applies `merge()`, then saves |
| `delete(Long id)` | DELETE | Read-write | **Soft delete**: sets `deletedAt` + `deletedBy`, does NOT remove the row |
| `bulkCreateOrUpdate(List)` | PUT | Read-write | Smart bulk: updates existing (by ID match), creates new |
| `bulkDelete(List<Long>)` | — | Read-write | Soft-deletes multiple entities by IDs |
| `search(String, Integer, Integer, String, String)` | GET | Read-only | Filter + paginate + sort + lazy-load includes |
| `search(SearchEntry)` | POST | Read-only | Same as above, but accepts a request body |

**Abstract method you must implement:**

```java
protected abstract Entity merge(Entity source, Entity target);
```

This dictates exactly how fields are patched during updates. The framework loads the existing entity, calls your `merge()`, then persists the result.

**The `includes` mechanism**: When `includes=roles,permissions` is passed, the framework uses Java Reflection to find getter methods matching those field names and forces Hibernate to initialize the lazy-loaded collections. This prevents `LazyInitializationException` without changing your fetch strategy.

---

### BaseController

**File**: `controllers/BaseController.java`

An abstract REST controller providing 7 pre-wired endpoints. Every endpoint is annotated with `@Authenticate`.

| Endpoint | Method | Description |
|---|---|---|
| `GET /{id}` | `findById` | Retrieve single entity by ID |
| `POST /` | `save` | Create a new entity |
| `PUT /{id}` | `update` | Update an existing entity |
| `DELETE /{id}` | `delete` | Soft-delete an entity |
| `PUT /bulk` | `bulkUpdate` | Bulk create or update a list of entities |
| `GET /search` | `search` | Dynamic search via query parameters |
| `POST /search` | `customSearch` | Dynamic search via request body (`SearchEntry`) |

**Abstract method you must implement:**

```java
protected abstract R createResponse(List<M> entryList);
```

This builds your custom response DTO from the result list.

**Search endpoint query parameters:**

| Parameter | Default | Description |
|---|---|---|
| `filters` | `null` | Dynamic filter string (see [Filter Syntax Reference](#filter-syntax-reference)) |
| `page` | `0` | Zero-based page index |
| `fetchSize` | `1000` | Number of results per page |
| `sortBy` | `null` | Sort expression (e.g., `createdAt:DESC`) |
| `includes` | `null` | Comma-separated lazy-loaded relations to initialize |

---

### BaseResponse & StatusResponse

**File**: `models/responses/BaseResponse.java`, `models/responses/StatusResponse.java`

Every API response extends `BaseResponse`:

```json
{
  "status": {
    "status_code": 200,
    "status_message": "Success",
    "status_type": "SUCCESS",
    "total_count": 42
  },
  "user_info": { ... },
  "data": [ ... ]
}
```

- **`status`** (`StatusResponse`): Contains `statusCode`, `statusMessage`, `statusType` (SUCCESS / ERROR / WARNING), and `totalCount`.
- **`userInfo`** (`Map<String, RealmUser>`): Populated by `@PopulateUserData` aspect — maps user IDs to user profiles.
- **`data`**: Your domain-specific payload (defined in your response subclass).

`StatusResponse` constructors accept `StatusCode` (interface implemented by `SuccessCodes` and `ErrorCodes`) and automatically set the `statusType` based on the code type.

---

### BaseMapper

**File**: `mappers/BaseMapper.java`

A MapStruct interface for entity merging:

```java
public interface BaseMapper<T extends BaseEntity> {
    T merge(T source, @MappingTarget T target);
}
```

When implementing `BaseService.merge()`, you can delegate to a MapStruct-generated mapper for clean, annotation-driven field mapping.

---

### IdGenerator

**File**: `models/IdGenerator.java`

A custom Hibernate `IdentityGenerator` that respects pre-set IDs:

- If the entity already has a non-null, positive `id` → **reuses** it (important for bulk upserts)
- Otherwise → delegates to the database's auto-increment sequence

---

## Search Engine

The search engine is the most powerful component of the framework, enabling frontends to construct complex SQL queries through URL parameters safely.

### SearchHelper

**File**: `utils/SearchHelper.java` (390 lines)

The central query parser. Key methods:

| Method | Description |
|---|---|
| `parseSearchParams(String filters)` | Parses the filter string into a `Map<SearchOperator, Map<String, String>>` |
| `getPredicatesFromSearchParams(...)` | Converts parsed params into JPA `Predicate[]` |
| `addPredicate(...)` | Builds a single predicate with type-aware handling for enums, dates, booleans, JSONB |
| `getPath(String key, Root root)` | Resolves field paths including nested joins via `-` delimiter |
| `getPageRequest(...)` | Builds a `Pageable` from page/fetchSize/sortBy parameters |

**Type-aware predicate building**: The engine inspects the Java type of each field path and applies the correct predicate logic:

- **Enums** → `Enum.valueOf()` conversion
- **Date / LocalDate / DateTime** → ISO-8601 parsing via `ISODateTimeFormat.dateTimeParser()`
- **Boolean** → Supports `1`/`0` shorthand (converts to `true`/`false`)
- **String (default)** → Direct string comparison

---

### SearchOperator Enum

**File**: `models/SearchOperator.java`

| Enum Value | URL Alias | SQL Equivalent |
|---|---|---|
| `EQUAL_TO` | `eq` | `=` |
| `NOT_EQUAL_TO` | `ne` | `!=` |
| `IS_NULL` | `isNull` | `IS NULL` |
| `IS_NOT_NULL` | `nn` | `IS NOT NULL` |
| `GREATER_THAN` | `gt` | `>` |
| `GREATER_THAN_EQUAL_TO` | `ge` | `>=` |
| `LESS_THAN` | `lt` | `<` |
| `LESS_THAN_EQUAL_TO` | `le` | `<=` |
| `LIKE` | `like` | `LIKE (lower)%` |
| `NOT_LIKE` | `nl` | `NOT LIKE %` |
| `IN` | `in` | `IN (...)` |
| `NOT_IN` | `nin` | `NOT IN (...)` |
| `JSONB_PATH_EXISTS` | `jsonb_path_exists` | `jsonb_extract_path_text IS NOT NULL` |
| `JSONB_PATH_EQUALS` | `jsonb_path_equals` | `jsonb_extract_path_text IN (...)` |
| `JSONB_PATH_CONTAINS` | `jsonb_path_contains` | `jsonb_extract_path_text LIKE %...%` |

---

### CustomSearchSpecification

**File**: `models/CustomSearchSpecification.java`

Implements Spring Data's `Specification<T>` interface, bridging the filter string to JPA criteria:

1. Splits the filter string on `__` to get **OR groups**
2. Within each group, parses filters (semicolon-separated) into **AND predicates**
3. Combines groups with `criteriaBuilder.or()`

**Logical combination:**
- `;` (semicolon) = **AND** within a group
- `__` (double underscore) = **OR** between groups

---

### SearchEntry

**File**: `models/entries/SearchEntry.java`

The request body for `POST /search`:

```json
{
  "filters": "status.eq:ACTIVE;age.ge:18",
  "page": 0,
  "fetch_size": 1000,
  "sort_by": "createdAt:DESC",
  "includes": "roles,permissions"
}
```

---

### Filter Syntax Reference

**Basic format**: `fieldName.OPERATOR:value`

**Combining filters:**
- **AND**: Separate with `;` → `status.eq:ACTIVE;age.ge:18`
- **OR**: Separate groups with `__` → `status.eq:ACTIVE__status.eq:PENDING`

**Complex examples:**

```
# Active users aged 18+ 
?filters=status.eq:ACTIVE;age.ge:18

# Find by multiple statuses (IN)
?filters=status.in:ACTIVE,PENDING,REVIEW

# Null check
?filters=deletedAt.isNull:

# Date range
?filters=createdAt.ge:2024-01-01T00:00:00Z;createdAt.lt:2024-12-31T23:59:59Z

# Like search (case-insensitive prefix match)
?filters=name.like:john

# Nested entity field via join (uses `-` as path separator)
?filters=department-name.eq:Engineering

# OR logic: active OR pending
?filters=status.eq:ACTIVE__status.eq:PENDING

# Combined: (active AND age > 18) OR (status = VIP)
?filters=status.eq:ACTIVE;age.gt:18__status.eq:VIP
```

---

### JSONB Query Support

For PostgreSQL JSONB columns, three special operators are available:

**`JSONB_PATH_EXISTS`** — Check if a path exists in the JSON:
```
?filters=metadata.jsonb_path_exists:address,city
```
→ SQL: `jsonb_extract_path_text(metadata, 'address', 'city') IS NOT NULL`

**`JSONB_PATH_EQUALS`** — Check if a path equals a value:
```
?filters=metadata.jsonb_path_equals:address,city|Mumbai,Delhi
```
→ SQL: `jsonb_extract_path_text(metadata, 'address', 'city') IN ('Mumbai', 'Delhi')`

**`JSONB_PATH_CONTAINS`** — Check if a path contains a substring:
```
?filters=metadata.jsonb_path_contains:address,city|Mum
```
→ SQL: `jsonb_extract_path_text(metadata, 'address', 'city') LIKE '%Mum%'`

**Delimiters:**
- Path tokens separated by `,`
- Path and value separated by `|`
- Multiple JSONB conditions with `&&`

---

### Sorting & Pagination

**Sort syntax**: `?sortBy=field:DIRECTION` where DIRECTION is `ASC` or `DESC`.

**Multi-field sorting**: Use commas → `?sortBy=createdAt:DESC,name:ASC`

**Pagination**: `?page=0&fetchSize=25`

If no `sortBy` is specified, results are returned in the database's natural order. Default `fetchSize` is `1000`.

---

### Nested Entity Joins

Use `-` (hyphen) as the path separator to query across JPA relationships:

```
?filters=department-name.eq:Engineering
```

This auto-generates a `JOIN` between the root entity and its `department` relation, then filters on the `name` field. Multi-level joins are supported:

```
?filters=department-organization-name.eq:Acme
```

→ Generates: `root.join("department").join("organization").get("name")`

---

## AOP Security & Context

### @Authenticate Annotation

**File**: `aop/Authenticate.java`

A method-level annotation (`@Target(ElementType.METHOD)`, `@Retention(RUNTIME)`) that triggers authentication when placed on any controller method. All `BaseController` endpoints use this by default.

---

### AuthenticateAspect

**File**: `aop/aspects/AuthenticateAspect.java`

An `@Around` aspect that intercepts every `@Authenticate`-annotated method:

**Flow:**

1. **Read namespace**: Extracts `x-namespace-id` header from the HTTP request (mandatory)
2. **Validate namespace**: Checks against `realm.supported-namespaces` configuration set
3. **Authenticate user** (if `realm.authentication.enabled` is `true`):
   - **Cookie auth**: Reads `sAccessToken` + `sIdRefreshToken` cookies → calls `RealmClient.getSessionInfo()`
   - **Client ID/Secret auth**: Reads `Client-Id` + `Client-Secret` headers → calls `RealmClient.verifyClientIdSecret()`
   - If neither is present → throws `AccessDeniedException`
4. **Set context**: Populates `Context.setNamespaceId()` and `Context.setUserId()`
5. **Execute method**: Calls `joinPoint.proceed()`
6. **Clean context**: Calls `Context.clean()` to prevent ThreadLocal leaks

**Configuration:**
```yaml
realm:
  authentication:
    enabled: true  # set to false to bypass auth
  supported-namespaces: namespace1,namespace2
```

---

### Context (ThreadLocal)

**File**: `utils/Context.java`

A static utility managing per-request state via `ThreadLocal`:

| Method | Description |
|---|---|
| `Context.getNamespaceId()` | Get the current tenant's namespace ID |
| `Context.setNamespaceId(String)` | Set the namespace (called by `AuthenticateAspect`) |
| `Context.getUserId()` | Get the authenticated user's ID |
| `Context.setUserId(String)` | Set the user ID |
| `Context.clean()` | Remove both ThreadLocal values (prevents memory leaks) |

You can call `Context.getUserId()` anywhere in your service layer without passing user IDs through method arguments.

---

### @PopulateUserData & UserDataAspect

**File**: `aop/PopulateUserData.java`, `aop/aspects/UserDataAspect.java`

A method-level annotation that automatically enriches API responses with user profile data.

**How it works:**

1. After the annotated method executes, the aspect intercepts the response
2. It calls `getData()` on the response via reflection
3. It scans the data class for fields annotated with `@UserField`
4. It extracts all user IDs from those fields using getter methods
5. It batch-fetches user profiles from Realm via `AuthService.getUsers()`
6. It sets the `userInfo` map on the response via `setUserInfo()`

**Result**: The API response automatically includes a `user_info` map keyed by user ID, containing full `RealmUser` profiles. This eliminates N+1 user lookups on the frontend.

**Configuration:**
```yaml
populate:
  user_data:
    enable: true  # set to false to disable
```

---

### @UserField Annotation

**File**: `aop/UserField.java`

A field-level annotation marking entity fields that contain user IDs. Used by `UserDataAspect` to identify which fields to resolve into full user profiles.

In `BaseEntity`, both `createdBy` and `updatedBy` are annotated with `@UserField`.

---

### @Client Annotation

**File**: `aop/Client.java`

A type-level annotation identical to `@Component`, acting as a semantic stereotype for client classes. Use it to mark classes that serve as integration clients.

---

### @FeignClientAdvice Annotation

**File**: `aop/FeignClientAdvice.java`

A type-level annotation that binds a Feign client to a custom exception handler. Example:

```java
@FeignClientAdvice(RealmExceptionHandler.class)
@FeignClient(name = "realm", url = "${realm.base-url}")
public interface RealmClient { ... }
```

---

## Multitenancy

### RoutingDataSource

**File**: `utils/RoutingDataSource.java`

Implements Spring's `AbstractRoutingDataSource` to route database connections based on the current tenant's namespace.

**How it works:**

1. On application boot, it creates a `HikariDataSource` for each configured data source
2. Maps each namespace to its corresponding data source
3. On every query, calls `Context.getNamespaceId()` to determine which pool to use
4. Sets the first configured data source as the default

**Factory methods:**
- `RoutingDataSource.of(propertiesList)` — Creates routing datasource **with** Flyway migrations
- `RoutingDataSource.ofJavers(propertiesList)` — Creates routing datasource **without** migrations (for audit)

---

### DataSourcePropertiesConfig & FaasDataSourceProperties

**File**: `configs/DataSourcePropertiesConfig.java`, `configs/FaasDataSourceProperties.java`

YAML-bound configuration for multitenant databases:

```yaml
data-sources:
  - url: jdbc:postgresql://host1:5432/db1
    username: user
    password: pass
    namespaces:
      - namespace1
      - namespace2
    minimum-idle: 5
    maximum-pool-size: 20
    flyway-migrate: true

  - url: jdbc:postgresql://host2:5432/db2
    username: user
    password: pass
    namespaces:
      - namespace3
    minimum-idle: 2
    maximum-pool-size: 10
    flyway-migrate: true

audit-data-sources:
  - url: jdbc:postgresql://host1:5432/audit_db
    username: user
    password: pass
    namespaces:
      - namespace1
      - namespace2
    minimum-idle: 2
    maximum-pool-size: 5
```

| Property | Description |
|---|---|
| `namespaces` | List of namespace IDs this datasource serves |
| `minimumIdle` | HikariCP minimum idle connections |
| `maximumPoolSize` | HikariCP maximum pool size |
| `flywayMigrate` | Whether to run Flyway migrations on boot (default: `false`) |

---

### Flyway Migrations

When `flywayMigrate` is `true`, the framework automatically runs Flyway migrations from `classpath:db/migration` for that datasource on application boot. Migrations use `baselineOnMigrate: true`, so they work on both fresh and existing databases.

---

## Authentication Service

**File**: `services/AuthService.java`

Handles authentication against the Realm (central auth) microservice.

| Method | Description |
|---|---|
| `authenticateCookieSession(List<Cookie>)` | Validates session cookies via `RealmClient.getSessionInfo()` |
| `authenticateClientIdSecret(String, String)` | Validates client credentials via `RealmClient.verifyClientIdSecret()` |
| `getUsers(Collection<String>)` | Batch-fetches user profiles with **EhCache caching** |

**User caching**: `getUsers()` checks the `realmUsers` EhCache before calling Realm. Only non-cached user IDs are fetched from the remote service. Fetched users are immediately cached for subsequent requests.

---

## Microservice Clients

### RealmClient

**File**: `clients/RealmClient.java`

A Spring Cloud OpenFeign client for the central authentication/user microservice:

| Endpoint | Method | Description |
|---|---|---|
| `GET /sessioninfo` | `getSessionInfo(cookie)` | Validate session and return user info |
| `GET /api/v1/clients/verify` | `verifyClientIdSecret(clientId, clientSecret)` | Validate client credentials |
| `POST /api/v1/whitelisted-users/search` | `getUsers(searchBody)` | Batch search users by IDs |

Uses `@FeignClientAdvice(RealmExceptionHandler.class)` for custom error handling.

---

### MercuryClient

**File**: `clients/MercuryClient.java`

A Spring Cloud OpenFeign client for the notification microservice:

| Endpoint | Method | Description |
|---|---|---|
| `POST /notify/sms` | `sendSMS(request)` | Send an SMS notification |
| `POST /notify/email` | `sendEmail(request)` | Send an email notification |
| `POST /notify/whatsapp` | `sendWhatsappMessage(request)` | Send a WhatsApp message |

**Conditional activation**: Only created when `mercury.base-url` is configured (`@ConditionalOnProperty`).

Uses `MercuryClientInterceptor` which automatically forwards the `x-namespace-id` header from the current `Context`.

---

### Client DTOs & Responses

The framework ships with pre-built request/response DTOs for both clients:

**Realm responses:**
- `RealmSessionInfoResponse` — Session validation result with user data
- `RealmClientsVerifyResponse` — Client credential verification result
- `RealmUserResponse` — Batch user search result with `whitelistedUsers` list
- `RealmUser` — User profile DTO

**Notification requests/responses:**
- `NotificationRequest` — Base notification request
- `SMSNotificationRequest` — SMS with `to`, `type`, `message`
- `EmailNotificationRequest` — Email with `sender`, `toAddresses`, `body`, `subject`, `html`, `ccAddresses`, `bccAddresses`, `charset`
- `WhatsappNotificationRequest` — WhatsApp with `provider`, contacts, template data
- `NotificationResponse` — Notification delivery confirmation

---

## Notification System

### NotificationService

**File**: `services/NotificationService.java`

A high-level service for sending notifications. Accepts clean DTOs and internally converts them to the appropriate request format.

| Method | Input DTO | Channel |
|---|---|---|
| `sendSMSNotification(SMSDto)` | `SMSDto` | SMS |
| `sendEmailNotification(EmailDto)` | `EmailDto` | Email |
| `sendWhatsappNotification(WhatsappDto)` | `WhatsappDto` | WhatsApp |

All methods automatically set `user` from `Context.getUserId()` and `clientCode` from `service.client-id` configuration.

**Conditional activation**: Only created when both `service.client-id` and `mercury.base-url` are configured.

---

### NotificationHandler Interface & Factory

**Files**: `notificationHandler/NotificationHandler.java`, `notificationHandler/NotificationHandlerFactory.java`

The notification system uses the **Factory Pattern**:

```java
// Interface
public interface NotificationHandler {
    <T extends NotificationRequest> NotificationResponse send(@NonNull T notificationRequest);
}

// Factory
NotificationHandler handler = factory.getNotificationHandler(NotificationType.SMS);
handler.send(request);
```

`NotificationHandlerFactory` dispatches to the correct handler based on `NotificationType`:
- `SMS` → `SMSNotificationHandler`
- `EMAIL` → `EmailNotificationHandler`
- `WHATSAPP` → `WhatsappNotificationHandler`

---

### SMS / Email / WhatsApp Handlers

Each handler implements `NotificationHandler` and delegates to the `MercuryClient`:

- `SMSNotificationHandler` → calls `mercuryClient.sendSMS()`
- `EmailNotificationHandler` → calls `mercuryClient.sendEmail()`
- `WhatsappNotificationHandler` → calls `mercuryClient.sendWhatsappMessage()`

---

## Content Management System (CMS)

### CmsService

**File**: `services/CmsService.java` (285 lines)

A comprehensive GraphQL-based CMS integration for SKU (Stock Keeping Unit) management:

| Method | Description |
|---|---|
| `getSkuByCodes(List<String>)` | Fetch SKUs by their codes, throws if any code is missing |
| `getSkuByAttributes(SkuAttributes)` | Find an exact SKU match by attributes |
| `getSkusByAttributes(SkuAttributes, Boolean)` | Find multiple similar SKUs |
| `createSku(SkuAttributes)` | Create a new SKU via GraphQL mutation + publish SNS event |
| `getOrCreateSku(SkuAttributes)` | **Find-or-create** semantics: tries to find, creates if not found |
| `skuSearch(CmsSearchRequestBody)` | Unified search: `SEARCH_BY_CODES`, `SEARCH_BY_ATTRIBUTES`, `SEARCH_ALIKE_SKUS` |
| `fetchSkuExpiryDate(String, DateTime)` | Calculate expiry date from SKU shelf life |

**GraphQL Client**: Uses `graphql-java-kickstart` WebClient with a custom `ConnectionProvider` (max 500 connections, 60s lifetime, 120s eviction).

**SNS Integration**: When a new SKU is created, a `SkuCreationTopicMessage` is published to the configured SNS topic.

---

### CmsController

**File**: `controllers/CmsController.java`

REST controller at `/cms`:

| Endpoint | Method | Description |
|---|---|---|
| `POST /cms/search` | `skuSearch` | Unified SKU search with multiple strategies |
| `POST /cms/find-or-create` | `findOrCreateSku` | Find existing or create new SKU |

Both endpoints are `@Authenticate`-protected.

---

### GraphQL Integration

The CMS service communicates with an external CMS via GraphQL queries stored as `.graphql` resource files:

- `searchSkuByCode.graphql` — Search by SKU codes
- `searchSku.graphql` — Search by attributes (full detail)
- `searchSkuLite.graphql` — Search by attributes (lightweight)
- `createSku.graphql` — Create a new SKU

GraphQL requests include the `saas-namespace` header for tenant identification.

---

### SNS Event Publishing

**File**: `events/SnsEventPublisher.java`

Publishes domain events to AWS SNS:

```java
public String publishToSkuCreationTopic(SkuCreationTopicMessage message)
```

Configuration:
```yaml
sns:
  topic:
    sku-creation: arn:aws:sns:region:account-id:sku-creation-topic
cloud:
  aws:
    region:
      static: ap-south-1
```

---

## Audit System (JaVers)

### AuditLogController

**File**: `audit/controllers/AuditLogController.java`

REST controller at `/audit`:

| Endpoint | Method | Description |
|---|---|---|
| `POST /audit/changes` | `audit` | Returns the complete change history for a given entity |

**Request body:**
```json
{
  "entity_id": 42,
  "entity_class": "com.example.Product"
}
```

**Response** includes:
- `auditDetails`: List of `AuditEntry` objects grouped by commit
- `userInfo`: Map of user IDs to `RealmUser` profiles (for who made each change)

---

### AuditLogService

**File**: `audit/services/AuditLogService.java`

Queries JaVers for entity change history:

1. Calls `javers.findChanges()` with an instance-based query
2. Groups changes by commit
3. For each commit, extracts property-level changes (`ChangeDetail`) with `left` (old) and `right` (new) values
4. Collects all author IDs and batch-fetches user profiles

Each `AuditEntry` contains:
- `commitId`, `commitAuthor`, `commitDate`
- `entityClass`, `entityId`
- `changes`: List of `ChangeDetail` with `property`, `changeType`, `left`, `right`

---

### JaVers Configuration

**Separate datasource**: JaVers uses its own `auditRoutingDataSource` (configured in `audit-data-sources`), distinct from the main application datasource. This allows audit data to be stored in a different database or schema.

**Author binding**: `JaversAuthorProvider` binds every JaVers commit to `Context.getUserId()`, ensuring audit trails correctly attribute changes to the authenticated user.

**Connection provider**: `JaversConnectionProvider` wraps a `DataSource` to provide JDBC connections for JaVers, using Spring's `DataSourceUtils` for proper transaction integration.

---

### Custom Type Adapters

JaVers custom adapters for serializing complex types:
- `DateTimeAdapter` — Joda `DateTime` serialization
- `JSONObjectAdapter` — `org.json.JSONObject` serialization
- `JsonNodeAdapter` — Jackson `JsonNode` serialization

---

## Exception Handling

### RestExceptionHandler

**File**: `exceptions/handlers/RestExceptionHandler.java`

A `@ControllerAdvice` that catches exceptions globally and wraps them in standardized `BaseResponse`:

| Exception | HTTP Status | Description |
|---|---|---|
| `Exception` (catch-all) | 500 | Generic internal error |
| `NoSuchElementException`, `EntityNotFoundException` | 204 | Resource not found |
| `ServiceException` | 500 | Custom service-level error |
| `MethodArgumentNotValidException` | 400 | Bean validation failures (field-level errors) |
| `RealmException` | Dynamic | Forwards Realm's HTTP status and reason |
| `HttpRequestMethodNotSupportedException` | 405 | Wrong HTTP method |
| `MissingPathVariableException` | 500 | Missing path variable |
| `NoHandlerFoundException` | 404 | No matching handler |

All responses follow the same JSON schema:
```json
{
  "status": {
    "status_code": 500,
    "status_message": "Error message here",
    "status_type": "ERROR"
  }
}
```

---

### gRPC Exception Handling

**Files**: `exceptions/handlers/BaseGrpcExceptionHandler.java`, `exceptions/handlers/GrpcServiceExceptionHandler.java`, `exceptions/interceptor/GrpcExceptionInterceptor.java`

A parallel exception handling system for gRPC endpoints:

| Exception | gRPC Status | Description |
|---|---|---|
| `ServiceException` | `INTERNAL` | Service-level errors |
| `NoSuchElementException`, `EntityNotFoundException` | `NOT_FOUND` | Resource not found |
| `Exception` (catch-all) | `INTERNAL` | Unknown errors |

Uses `@GrpcExceptionHandler` annotations and a custom `GrpcExceptionInterceptor` for interception.

---

### Feign Client Exception Handling

**Files**: `exceptions/CustomErrorDecoder.java`, `exceptions/handlers/feignClientExceptionHandlers/RealmExceptionHandler.java`

Custom error decoding for Feign client calls. The `@FeignClientAdvice` annotation binds a client to its exception handler, which can translate HTTP errors from downstream services into framework-specific exceptions.

---

### Custom Exception Classes

| Exception | Purpose |
|---|---|
| `ServiceException` | General service-layer errors |
| `AccessDeniedException` | Authentication/authorization failures |
| `NotFoundException` | Resource not found |
| `NotAllowedException` | Operation not permitted |
| `ValidationException` | Bean validation failures |
| `CmsException` | CMS integration errors |
| `CacheNotFoundException` | Missing cache configuration |
| `RealmException` | Realm service errors (wraps Feign response) |
| `FeignBaseException` | Base for Feign client errors |
| `GeneralException` | Generic uncategorized errors |
| `InternalAuthenticationServiceException` | Internal auth failures |

---

## Auto-Configuration

### RestConfig (ObjectMapper)

**File**: `configs/RestConfig.java`

Configures the global Jackson `ObjectMapper`:

| Setting | Value | Description |
|---|---|---|
| Naming strategy | `SNAKE_CASE` | All JSON fields use snake_case |
| Serialization inclusion | `ALWAYS` | Include all fields, even null |
| Dates as timestamps | `false` | Dates serialized as ISO strings |
| Timezone | `Asia/Kolkata` | IST timezone for date rendering |
| Unknown properties | `FAIL = false` | Ignore unknown fields during deserialization |
| Single value as array | `true` | Accept single values where arrays are expected |
| Unquoted control chars | `true` | Lenient JSON parsing |
| Registered modules | Hibernate5, JsonOrg, JavaTime, Joda | Full type support |

---

### SwaggerConfig (OpenAPI 3.0)

**File**: `configs/SwaggerConfig.java`

Pre-configured Swagger/OpenAPI documentation:

- **Specification**: OAS 3.0
- **Active profiles**: All except `prod` (`@Profile({"!prod"})`)
- **Security**: API key via `Authorization` header
- **Media types**: JSON only
- **URL templating**: Enabled
- Includes a `BeanPostProcessor` to fix Springfox + Spring Boot 2.6+ compatibility

---

### GrpcConfig

**File**: `configs/GrpcConfig.java`

A global gRPC server interceptor that:
1. Reads the `X-Requested-By` metadata header
2. Sets `Context.setUserId()` with the header value
3. Falls back to `"System"` if the header is missing

This ensures that gRPC requests populate the same `Context` as REST requests.

---

### CacheConfig (EhCache)

**File**: `configs/CacheConfig.java`

Enables Spring caching with EhCache:
- Loads configuration from `classpath:ehcache.xml`
- Defines the `REALM_USER_CACHE` cache name constant used by `AuthService`

---

### FeignClientConfig

**File**: `configs/FeignClientConfig.java`

A global Feign `RequestInterceptor` that automatically adds:
- `Content-Type: application/json`
- `Accept: application/json`
- `saas-namespace` header from `Context.getNamespaceId()`
- `Client-Id` and `Client-Secret` from service configuration

This ensures inter-service calls carry authentication context.

**Configuration:**
```yaml
service:
  client-id: your-service-client-id
  client-secret: your-service-client-secret
```

---

### SnsConfig (AWS SNS)

**File**: `configs/SnsConfig.java`

Initializes an `AmazonSNS` client on application boot using the configured AWS region:

```yaml
cloud:
  aws:
    region:
      static: ap-south-1
```

---

### ApplicationConfig

**File**: `configs/ApplicationConfig.java`

Registers `HttpRequestInterceptor` as a Spring MVC interceptor, excluding `/actuator/**` paths. This interceptor logs incoming HTTP requests.

---

### RabbitConfig

**File**: `configs/RabbitConfig.java`

A placeholder configuration for RabbitMQ integration. The actual beans (RabbitTemplate, MessageConverter) are commented out but the structure is ready for activation.

---

## Utility Classes

### HttpUtils & CookieUtils

- **`HttpUtils`**: Retrieves the current `HttpServletRequest`, reads mandatory and optional headers
- **`CookieUtils`**: Reads specific cookies from HTTP requests (used by `AuthenticateAspect`)

### ReflectionUtils

Field/method introspection utilities used by `UserDataAspect`:
- Find annotated fields
- Map fields to getter/setter methods
- Cast objects to lists

### StringUtils

String manipulation utilities including:
- Concatenation with separators
- Extracting Realm IDs from data objects via getter methods

### ValidationUtils

Programmatic bean validation using `javax.validation`:

```java
ValidationUtils.validate(object); // throws ValidationException if invalid
```

Uses the default `ValidatorFactory` and collects all constraint violations into a single error message.

### GraphQLUtils & CmsUtils

- **`GraphQLUtils`**: Resolves GraphQL query file paths from resources
- **`CmsUtils`**: SKU attribute enrichment, default shelf life calculation, quantity attribute handling

### ProtoMapperUtil

Utility for mapping between Protocol Buffer objects and Java POJOs (for gRPC integration).

### UomUtils & UnitOfMeasurement

- **`UnitOfMeasurement`**: Enum defining units (e.g., KG, PIECE, etc.)
- **`UomUtils`**: Conversion utilities between units of measurement

### HttpRequestInterceptor & SqlStatementInterceptor

- **`HttpRequestInterceptor`**: Logs incoming HTTP requests (registered in `ApplicationConfig`)
- **`SqlStatementInterceptor`**: Hibernate SQL statement interceptor for monitoring/logging

---

## Status Codes

### ErrorCodes

| Code | Name | Message |
|---|---|---|
| 101 | `GENERIC_ERROR_OCCURRED` | Error Occurred! |
| 102 | `NOT_FOUND` | Data not found |
| 400 | `BAD_REQUEST` | Internal Server Error |

### SuccessCodes

| Code | Name | Message |
|---|---|---|
| 101 | `DATA_RETRIEVED_SUCCESSFULLY` | Data retrieved Successfully |
| 102 | `APP_CONFIG_CREATED_SUCCESSFULLY` | App config created successfully |
| 200 | `OK` | Success |
| 201 | `CREATED` | Created Successfully |

---

## Configuration Properties Reference

| Property | Required | Default | Description |
|---|---|---|---|
| `realm.base-url` | Yes | — | Realm (auth) service base URL |
| `realm.authentication.enabled` | No | `true` | Enable/disable authentication |
| `realm.supported-namespaces` | Yes | — | Comma-separated list of valid namespace IDs |
| `service.client-id` | Yes | — | This service's client ID for inter-service auth |
| `service.client-secret` | Yes | — | This service's client secret |
| `mercury.base-url` | No | — | Mercury (notification) service base URL |
| `cms.base-url` | No | — | CMS service base URL |
| `cms.namespace-id` | No | — | Namespace ID for CMS requests |
| `cloud.aws.region.static` | No | — | AWS region for SNS |
| `sns.topic.sku-creation` | No | — | SNS topic ARN for SKU creation events |
| `populate.user_data.enable` | No | `true` | Enable/disable user data population |
| `data-sources` | Yes | — | List of tenant database configurations |
| `audit-data-sources` | No | — | List of audit database configurations |

---

## Build & Publish

### Build

```bash
./gradlew build
```

### Publish

The project is configured with `maven-publish`. Published coordinates:

```
groupId:    com.github.ironman19933
artifactId: service-framework
version:    4.0.1
```

JitPack builds are configured via `jitpack.yml` to use **OpenJDK 11**.

---

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## License

No license file is currently included. Please contact the maintainer for licensing information.
