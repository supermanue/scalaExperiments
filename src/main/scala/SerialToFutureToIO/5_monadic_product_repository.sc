import cats.effect.{IO, Sync}
import cats.implicits._

import scala.collection.mutable

case class Product (id: String, sells: Int)


trait ProductRepository[M[_]] {
  def findProduct(productId: String): M[Option[Product]]

  def saveProduct(product: Product): M[Option[Product]]

  def incrementProductSells(productId: String, quantity: Int): M[Option[Product]]

  def fail(): M[Unit]
}


class ProductRepositoryImpl[M[_]: Sync] extends ProductRepository[M] {

  val storage = mutable.HashMap[String, Product]()

  override def findProduct(productId: String): M[Option[Product]] = Sync[M].delay(storage.get(productId))

  override def saveProduct(product: Product): M[Option[Product]] = Sync[M].delay(storage.put(product.id, product))

  override def incrementProductSells(productId: String, quantity: Int): M[Option[Product]] =
    for {
      maybeProduct <- findProduct(productId)
      newProduct = maybeProduct.map(product => Product(product.id, product.sells + quantity))
      result <- newProduct.fold(Sync[M].pure[Option[Product]](None))(product => saveProduct(product))
    } yield result

  override def fail(): M[Unit] =  Sync[M].delay(new Exception("exception in IO"))
}

val myRepo: ProductRepository[IO] = new ProductRepositoryImpl[IO]()


//happy path: we create a product, store, update and retrieve
val productA = Product("a", 0)
val resHappy = for {
  _ <- myRepo.saveProduct(productA)
  _ <-  myRepo.incrementProductSells("a", 5)
  res <- myRepo.findProduct("a")
} yield res
resHappy.unsafeRunSync()

//happy path: retrieve a product that does not exist
val failureResult = for {
  res <- myRepo.findProduct("b")
} yield res
val res1 = failureResult.unsafeRunSync()

//unhappy path: exception
val exceptionResult = for {
  res <- myRepo.fail()
} yield res
val res2 = exceptionResult.unsafeRunSync() //uncomment this to test



//resFor and res2For are both executed
val resFut =  myRepo.findProduct("a")
val tmpFut =  myRepo.fail()
val res2Fut =  myRepo.findProduct("b")

val results = for {
  resFor <- resFut
  tmpFor <- tmpFut
  res2For <- res2Fut
} yield (resFor, res2For)

val res3 = results.unsafeRunSync()