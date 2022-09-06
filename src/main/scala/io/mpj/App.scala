package io.mpj
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.methods.response.Web3ClientVersion
import org.web3j.protocol.http.HttpService
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Transfer
import org.web3j.utils.Convert


/**
 * @author ${user.name}
 */
object App {
  
  def foo(x : Array[String]) = x.foldLeft("")((a,b) => a + b)
  
  def main(args : Array[String]) {
    println( "Hello World!" )
    println("concat arguments = " + foo(args))


    // get the eth client version
    val mainnet = "https://mainnet.infura.io/v3/c6808de6478048a9988410446bb3d988"
    val goerli = "https://goerli.infura.io/v3/c6808de6478048a9988410446bb3d988"
    val web3 = Web3j.build(new HttpService(goerli)) // defaults to http://localhost:8545/
    val web3ClientVersion = web3.web3ClientVersion.send
    val clientVersion = web3ClientVersion.getWeb3ClientVersion
    println("clientVersion = " + clientVersion)


    // send eth
    val source = scala.io.Source.fromFile("/Users/abdurrachmanmappuji/Development/empeje/web3-intro/keys/goerli")
    val lines = source.getLines.mkString

    println(lines)

    val credentials = Credentials.create(lines)
    val amount = BigDecimal(1.0).bigDecimal
    val transactionReceipt = Transfer.sendFunds(web3, credentials, "0x20B35d6E0C4accf0391FD8fA98bBf63d737fC8AA", amount, Convert.Unit.GWEI).send
    println("transactionReceipt = " + transactionReceipt)
  }

}
