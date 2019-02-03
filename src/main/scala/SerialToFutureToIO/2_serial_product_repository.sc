import scala.collection.mutable

case class Product (id: String, sells: Int)


trait ProductRepository {
  def findProduct(productId: String): Option[Product]

  def saveProduct(product: Product): Option[Product]

  def incrementProductSells(
    productId: String, quantity: Int): Option[Product]
}


class ProductRepositoryImpl extends ProductRepository {

  val storage = mutable.HashMap[String, Product]()

  override def findProduct(productId: String): Option[Product] = storage.get(productId)

  override def saveProduct(product: Product): Option[Product] = storage.put(product.id, product)

  override def incrementProductSells(productId: String, quantity: Int): Option[Product] = {
    findProduct(productId)
      .flatMap(product => saveProduct(
        Product(product.id, product.sells + quantity)))

  }

}

val myRepo: ProductRepository = new ProductRepositoryImpl()
val productA = Product("a", 0)

myRepo.saveProduct(productA)
myRepo.incrementProductSells("a", 5)
val result = myRepo.findProduct("a")

