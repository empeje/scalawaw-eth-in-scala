package io.mpj
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.methods.response.Web3ClientVersion
import org.web3j.protocol.http.HttpService
import org.web3j.crypto.{Credentials, RawTransaction, TransactionEncoder, WalletUtils}
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
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
    // TODO: disable the key after presentation
    val mainnet = "https://mainnet.infura.io/v3/c6808de6478048a9988410446bb3d988"
    val goerli = "https://goerli.infura.io/v3/c6808de6478048a9988410446bb3d988"
    val web3 = Web3j.build(new HttpService(goerli)) // defaults to http://localhost:8545/
    val web3ClientVersion = web3.web3ClientVersion.send
    val clientVersion = web3ClientVersion.getWeb3ClientVersion
    println("clientVersion = " + clientVersion)


    // prepare credentials
    val source = scala.io.Source.fromFile("/Users/abdurrachmanmappuji/Development/empeje/web3-intro/keys/goerli")
    val lines = source.getLines.mkString
    val credentials = Credentials.create(lines)

    // send eth
    /*
    val amount = BigDecimal(1.0).bigDecimal
    val transactionReceipt = Transfer.sendFunds(web3, credentials, "0x20B35d6E0C4accf0391FD8fA98bBf63d737fC8AA", amount, Convert.Unit.GWEI).send
    println("transactionReceipt = " + transactionReceipt)
    */

    // manual transaction
    // get the next available nonce

    /*
    // if error usually because of nonce
    val ethGetTransactionCount = web3.ethGetTransactionCount("0x20B35d6E0C4accf0391FD8fA98bBf63d737fC8AA", DefaultBlockParameterName.PENDING).send();
    var nonce = ethGetTransactionCount.getTransactionCount();
    println("nonce = " + nonce)
    nonce = BigInt(9).bigInteger // manually set nonce if needed

    // create our transaction
    val rawTransaction = RawTransaction.createEtherTransaction(
      nonce,
      BigInt(21000).bigInteger, // gasPrice
      BigInt(21000).bigInteger, // gasLimit
      "0x20B35d6E0C4accf0391FD8fA98bBf63d737fC8AA",
      BigInt(1200).bigInteger);
    println("rawTransaction = " + rawTransaction)
    // sign & send our transaction
    val signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
    val hexValue = convertBytesToHex(signedMessage);
    println("hexValue = " + hexValue)
    val ethSendTransaction = web3.ethSendRawTransaction(hexValue).send;

    println("ethSendTransaction = " + ethSendTransaction.getTransactionHash)
    println("ethSendTransaction = " + ethSendTransaction.getError)
    println(ethSendTransaction.getError.getMessage)
   */

    // interact with smart contract
    val contract = Usdc.load(
      "0x2f3A40A3db8a7e3D09B0adfEfbCe4f6F81927557", web3,
        credentials, BigInt(9).bigInteger, BigInt(21000).bigInteger);
    val coinName = contract.name().send();
    println("transactionReceipt = " + coinName)
    val symbol = contract.symbol().send();
    println("symbol = " + symbol)

    val totalSupply = contract.totalSupply().send();
    println("totalSupply = " + totalSupply)

    val balance = contract.balanceOf("0x20B35d6E0C4accf0391FD8fA98bBf63d737fC8AA").send();
    println("balance = " + balance)

    val balanceOfOther = contract.balanceOf("0x0cbdf5b0c4e117619631ba4b97dc0d439adabd88").send();
    println("balanceOfOther = " + balanceOfOther)
  }

  def convertBytesToHex(bytes: Seq[Byte]): String = {
    val sb = new StringBuilder
    sb.append("0x")
    for (b <- bytes) {
      sb.append(String.format("%02x", Byte.box(b)))
    }
    sb.toString
  }
}
