import cats.effect.IO

import scala.collection.mutable

case class Product (id: String, sells: Int)


trait ProductRepository {
  def findProduct(productId: String): IO[Option[Product]]

  def saveProduct(product: Product): IO[Option[Product]]

  def incrementProductSells(productId: String, quantity: Int): IO[Option[Product]]

  def fail(): IO[Unit]
}


class ProductRepositoryImpl extends ProductRepository {

  val storage = mutable.HashMap[String, Product]()

  override def findProduct(productId: String): IO[Option[Product]] = IO(storage.get(productId))

  override def saveProduct(product: Product): IO[Option[Product]] = IO(storage.put(product.id, product))

  override def incrementProductSells(productId: String, quantity: Int): IO[Option[Product]] =
    for {
      maybeProduct <- findProduct(productId)
      newProduct = maybeProduct.map(product => Product(product.id, product.sells + quantity))
      result <- newProduct.fold(IO[Option[Product]](None))(product => saveProduct(product))
    } yield result

  override def fail(): IO[Unit] =  IO.raiseError(new Exception("exception in IO"))
}

val myRepo: ProductRepository = new ProductRepositoryImpl()


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
failureResult.unsafeRunSync()

//unhappy path: exception
val exceptionResult = for {
  res <- myRepo.fail()
} yield res
//exceptionResult.unsafeRunSync() //uncomment this to test



//Solution to problem in FUTURE. Here we know that res2Fut was not executed
val resFut =  myRepo.findProduct("a")
val tmpFut =  myRepo.fail()
val res2Fut =  myRepo.findProduct("b")

val results = for {
  resFor <- resFut
  tmpFor <- tmpFut
  res2For <- res2Fut
} yield (resFor, res2For)

results.unsafeRunSync()