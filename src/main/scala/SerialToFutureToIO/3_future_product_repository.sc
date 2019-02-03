import scala.collection.mutable
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

case class Product (id: String, sells: Int)


trait ProductRepository {
  def findProduct(productId: String): Future[Option[Product]]

  def saveProduct(product: Product): Future[Option[Product]]

  def incrementProductSells(productId: String, quantity: Int): Future[Option[Product]]

  def fail(): Future[Unit]
}


class ProductRepositoryImpl(implicit executor: ExecutionContext) extends ProductRepository {

  val storage = mutable.HashMap[String, Product]()

  override def findProduct(productId: String): Future[Option[Product]] = Future(storage.get(productId))

  override def saveProduct(product: Product): Future[Option[Product]] = Future(storage.put(product.id, product))

  override def incrementProductSells(productId: String, quantity: Int): Future[Option[Product]] =
  for {
    maybeProduct <- findProduct(productId)
    newProduct = maybeProduct.map(product => Product(product.id, product.sells + quantity))
    result <- newProduct.fold(Future[Option[Product]](None))(product => saveProduct(product))
  } yield result

  override def fail(): Future[Unit] = Future.failed(new Exception("exception in Future"))
}

//FUTURE requires an executionContext
import scala.concurrent.ExecutionContext.Implicits.global

val myRepo: ProductRepository = new ProductRepositoryImpl()


//happy path: we create a product, store, update and retrieve
val productA = Product("a", 0)
myRepo.saveProduct(productA)
myRepo.incrementProductSells("a", 5)
val resHappy = myRepo.findProduct("a")
Await.result(resHappy, Duration.Inf)

//happy path: retrieve a product that does not exist
val failureResult = myRepo.incrementProductSells("b", 5)
Await.result(failureResult, Duration.Inf)

//unhappy path: exception in future
/*
val exception = myRepo.fail()
Await.result(exception, Duration.Inf)
*/


//problem: res & res2 are executed but we cannot get res2 output
val resFut =  myRepo.findProduct("a")
val tmpFut =  myRepo.fail()
val res2Fut =  myRepo.findProduct("b")

val results = for {
  resFor <- resFut
  tmpFor <- tmpFut
  res2For <- res2Fut
} yield (resFor, res2For)

Await.result(results, Duration.Inf)
