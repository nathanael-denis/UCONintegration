package iotawucon;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import org.iota.client.BalanceAddressResponse;
import org.iota.client.Client;
import org.iota.client.GetAddressesBuilder;
import org.iota.client.Message;
import org.iota.client.MessageId;
import org.iota.client.MessageMetadata;
import org.iota.client.NodeInfoWrapper;
import org.iota.client.OutputsOptions;
import org.iota.client.SecretKey;
import org.iota.client.UtxoInput;
import org.iota.client.local.ClientException;
public class IOTAUtils {
	private static final int HTTPSPORT = 441;
	private static final int APIPORT = 14265;
	private static final String SEED="SUPERSECURESEEDREALLY";
	private static final int MIOTA = 1000000;
	
	 /**
	  * basic client to connect to a remote node.
	  * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	  */
	 static Client remoteNode() throws FileNotFoundException, IOException {
		 	//String URL = "https://chrysalis-nodes.iota.cafe:443"; //Devnet node
	    	String currentDir = System.getProperty("user.dir");
	    	
	     	String appConfigPath = currentDir+ "/config.properties";
	     	Properties appProps = new Properties();
	     	appProps.load(new FileInputStream(appConfigPath));
		 	String URLNODE=appProps.getProperty("URLNODE");
		 	System.out.println(URLNODE);
			//String URL = "http://3.90.201.158:14265"; // AWS node
		
	        Client iota = Client.Builder().withNode(URLNODE) // Insert your node URL here
	                // .withNodeAuth("https://somechrysalisiotanode.com", "jwt_or_null",
	                // "name_or_null", "password_or_null") //
	                // Optional authentication
	                .finish();
	        return iota;
	    }
	  /**
	  * remote node information,
	  * maintained by the IOTA foundation.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	  */
	    public static void remoteNodeInfo() throws FileNotFoundException, IOException {
	        try {
	            Client iota = remoteNode();

	            System.out.println("Node healthy: " + iota.getHealth());

	            NodeInfoWrapper info = iota.getInfo();
	            System.out.println("Node url: " + info.url());
	            System.out.println("Node Info: " + info.nodeInfo());
	        } catch (ClientException e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	    }
	    /**
	     * get the information of the node
	     * locally deploy the UCS.
	     * @return the Client information.
	     */
	    public static Client localNode() {
	    	//String URL = "http://localhost:14266";
	    	String URL = "http://127.0.0.1:14265";
	        Client iota = Client.Builder().withNode(URL) // Insert your node URL here
	                // .withNodeAuth("https://somechrysalisiotanode.com", "jwt_or_null",
	                // "name_or_null", "password_or_null") //
	                // Optional authentication
	                .finish();
	        return iota;
	    }
	    
		  /**
		  * local node information, exposed
		  * chrysalis node for load balancing.
		  */
		    public static void localNodeInfo() {
		        try {
		            Client iota = localNode();

		            System.out.println("Node healthy: " + iota.getHealth());

		            NodeInfoWrapper info = iota.getInfo();
		            System.out.println("Node url: " + info.url());
		            System.out.println("Node Info: " + info.nodeInfo());
		        } catch (ClientException e) {
		            System.out.println("Error: " + e.getMessage());
		        }
		    }
		    
		    /**
		     * basic client to connect to the
		     * development network.
		     * @return the Client information.
		     */
		    public static Client remoteDevnetNode() {
		    	String URL = "https://api.lb-0.h.chrysalis-devnet.iota.cafe/ ";
		        Client iota = Client.Builder().withNode(URL)
		                .finish();
		        return iota;
		    }
			  /**
			  * devnet node information, ran by
			  * the IOTA foundation.
			  */
		    public static void remoteDevnetNodeInfo() {
		    	try {
		    		Client iota = remoteDevnetNode();
		            System.out.println("Node healthy: " + iota.getHealth());
		            NodeInfoWrapper info = iota.getInfo();
			        System.out.println("Node url: " + info.url());
			        System.out.println("Node Info: " + info.nodeInfo());
			        } catch (ClientException e) {
			            System.out.println("Error: " + e.getMessage());
			        }
			    }
		    
		    /**
		     * Generate several addresses associated to one seed.
		     * @return 10 generated addresses
		     */
		    public static String[] generateUsableAddress() {
		    	SecretKey secret_key = SecretKey.generate();
		    	Client iota = remoteDevnetNode();
		    	String[] addresses = new GetAddressesBuilder(secret_key.toString()).withClient(iota).withRange(0, 10).finish();
		    	System.out.println(Arrays.toString(addresses));
				return addresses;
		    }

		    /**
		     * Will return the last
		     * four tips of the devnet node.
		     * @return the four last tips as a string[]
		     */
		    public static String[] getTipsFromDevNode() {
		    	Client iota = remoteDevnetNode();
		    	String[] tips = iota.getTips();
		    	return tips;
		    }
		   /**
		    *  This method will return the amount of
		    *  iota associated with a given address,
		    *  using the devnet load balancer.
		    * @param address to look up
		    * @return the response holding the balance
		    */
		   public static BalanceAddressResponse getBalanceDevnet(String address) {
			   Client iota = remoteDevnetNode();
			   return iota.getAddressBalance(address);
		   }
		   /**
		    * send a message on the devnet using the load balancer.
		    */
		   public static void sendEmptyMessageDevnet() {
			   Client iota = remoteDevnetNode();
			Message messageToSend = iota.message().finish();
			MessageMetadata metadata = iota.getMessage().metadata(messageToSend.id());
			System.out.println("Message metadata: " + metadata);
			Message message = iota.message().withIndexString("Hello").withDataString("Tangle").finish();
			System.out.println("Message sent to devnet" + message.id());
			MessageId[] fetched_message_ids = iota.getMessage().indexString("Hello");
			System.out.println("Messages with Hello index: " + Arrays.toString(fetched_message_ids));
			   
		   }
		   /**
		    * sends an empty message using the local node.
		    */
		   public static void sendEmptyMessageLocalNode() {
			Client iota = localNode();
			Message messageToSend = iota.message().finish();
			MessageMetadata metadata = iota.getMessage().metadata(messageToSend.id());
			System.out.println("Message metadata: " + metadata);
			Message message = iota.message().withIndexString("Hello").withDataString("Tangle").finish();
			System.out.println("Message sent to devnet" + message.id());
			MessageId[] fetched_message_ids = iota.getMessage().indexString("Hello");
			System.out.println("Messages with Hello index: " + Arrays.toString(fetched_message_ids));
			   
		   }
		    /**
		     * transaction on the devnet. Takes the first address associated to
		     * the seed as the buyer and the given brokerAddr as destination.
		     * Note that the node will refuse the transaction if the balance has
		     * insufficient funds, which means we can keep the information locally.
		     * @param seed, enable to generate the buyer address from seed
		     * and have the private keys necessary for the transaction.
		     * @param brokerAddr
		     * @param value
		     * @throws IOException 
		     * @throws FileNotFoundException 
		     */
		    public static String[] transaction(String seed, String brokerAddr, int value, String node) throws FileNotFoundException, IOException {
		    	Client iota = null;
				if (node.equals("LOCALHOST")) {
		    		iota  = localNode();
		    	}
				if (node.equals("REMOTE")) {
		    		//iota  = remoteDevnetNode();
					iota = remoteNode();
		    	}
		        GetAddressesBuilder addresses = iota.getAddresses(seed).withRange(0, 1);
		        String[] addresses_str = addresses.finish();
		        System.out.println("Buyer address generated :" + addresses_str[0]);
		        try
		        {
			        Message message = iota
				            .message()
				            .withSeed(SEED)
				            // Insert the output address and amount to spent. The amount cannot be zero.
				            .withOutput(
				                // We generate an address from our seed so that we send the funds to ourselves
				                        brokerAddr, 1000000
				            ).finish();

		        
		        System.out.println("Transaction Id on network" + iota.getMessage().data(message.id()));
		        //System.out.println("Transaction sent: https://explorer.iota.org/devnet/message/" +  message.id());
		        System.out.println("Transaction sent: http://localhost:8082/explorer/search/" +  message.id());
		        // The transaction was validated and sent to the network, we store the transaction locally to
		        // reuse it. For that, we catch the args and send them to the PAP service.
		        
		        String[] args = new String[4];
		        args[0] = addresses_str[0]; //buyer address
		        args[1] = brokerAddr;
		        args[2] = String.valueOf(value);
		        args[3] = message.id().toString();
		        return args;
	        }
	        catch(Exception ex)
	        {
	        	System.out.println("Node discontinued");
	              //do error handling
	        	String[] args = new String[4];
		        args[0] = addresses_str[0]; //buyer address
		        args[1] = brokerAddr;
		        args[2] = String.valueOf(value);
		        args[3] = "Error transaction";
		        return args;
	        }

		    }
		    /**
		     * used in remote setting to get message info query time,
		     * not having it locally. Not relevant in local
		     * setting since the UCS keeps the arguments (when used,
		     * query time is under 1ms in local setting).
		     * @param messageId the resulting id of the transaction.
		     * @return
		     */
		    public static void fetchMessage() {
		    	Client iota = remoteDevnetNode();
		    	Message message = iota
			            .message()
			            .withSeed(SEED).finish();
		    	long start_query= System.currentTimeMillis();
		    	Message data = iota.getMessage().data(message.id());
		    	long finish_query = System.currentTimeMillis();
		    	System.out.println(data);
		    	
		    	// Query time is about twice the time to ping 
		    	// the node. Takes 1ms for local node
		    	long query_time = finish_query-start_query;
		    	System.out.println(query_time);
		    }
		    /**
		     * generate a transaction then fetch it and print the time necessary for
		     * the operation. It does take into account the connection to the node since
		     * @param value
		     * @param node
		     * @throws IOException 
		     * @throws FileNotFoundException 
		     */
		    public static void fetchTransactionTime(String seed, String brokerAddr, int value, String node) throws FileNotFoundException, IOException {
		    		Client iota = null;
					if (node.equals("LOCALHOST")) {
			    		iota  = localNode();
			    	}
					if (node.equals("REMOTE")) {
			    		//iota  = remoteDevnetNode();
						iota = remoteNode();
			    	}
			        GetAddressesBuilder addresses = iota.getAddresses(seed).withRange(0, 1);
			        Message message = iota
			            .message()
			            .withSeed(seed)
			            .withOutput(
			            		brokerAddr, value
			            ).finish();
			    	long start_query= System.currentTimeMillis();
			    	Message fetch = iota.getMessage().data(message.id());
			    	long finish_query = System.currentTimeMillis();
			    	
			    	// Query time is about twice the time to ping 
			    	// the node. Takes 1ms for local node
			    	long query_time = finish_query-start_query;
		    }
		    public static void processTransactionTime(String brokerAddress, int value) {
		    	/* Node used to connect and fetch the transaction,
		    	 * does not impact the UCS processing accessed in
		    	 * this function.
		    	 */
		    	Client iota = remoteDevnetNode(); 

		        GetAddressesBuilder addresses = iota.getAddresses(SEED).withRange(0, 1);
		        // Create and send the message to the node, not taken 
		        // into account for time measure as well.
		        Message message = iota
		            .message()
		            .withIndexString(brokerAddress)
		            .withSeed(SEED)
		            .withOutput(
		            		brokerAddress, value
		            ).finish();
		        // fetch the message
		        MessageId messageId = message.id();
		        System.out.println(messageId);
		    	Message toBeProcessed = iota.getMessage().data(message.id());
		    	System.out.println(toBeProcessed);
		    	long start_processing= System.currentTimeMillis();
		        String fetch_serialised = toBeProcessed.payload().get().serialize(); //almost 0
		        Boolean amount = fetch_serialised.contains("100000"); //almost 0
		        MessageId[] fetched_message_ids = iota.getMessage().indexString(brokerAddress);
		        int length = fetched_message_ids.length;
		        Boolean rightAddr= false;
		        if (messageId.equals(fetched_message_ids[0])) {
		        	rightAddr=true;
		        }
		        Boolean transactionIsValid = amount & rightAddr;
		    	long finish_processing = System.currentTimeMillis();
		    	long processing_time = finish_processing - start_processing;
		    	System.out.println(fetched_message_ids);
		    	System.out.println(fetch_serialised);
		    	System.out.println(processing_time);
		        System.out.println(amount);
		        System.out.println(transactionIsValid);
		    }
		    /**
		     * since we can't interact with the faucet algorithmically,
		     * we turn the first address of the reference SEED into a faucet 
		     * and then distribute to the other addresses.
		     * @param n the number of addresses to feed with iotas.
		     * @throws InterruptedException 
		     * @throws IOException 
		     * @throws FileNotFoundException 
		     */
		    public static void homeMadeFaucet(int n) throws InterruptedException, FileNotFoundException, IOException {
	    		Client iota = remoteDevnetNode();
	    		// First address is the faucet address. 1 to n are the buyer addresses.
	    		GetAddressesBuilder addresses = iota.getAddresses(SEED).withRange(0, n);
	    		String[] addresses_str = addresses.finish();
	    		for( int i = 1; i < n; i++) {
	    			System.out.println(addresses_str[i]);
	    			Thread.sleep(2); // avoid conflicts on Tangle
	    			transaction(SEED, addresses_str[i], 1000000, "REMOTE");
	    		}
	    	}
		    
		    /**
		     * Generate a given number of addresses, serve as a
		     * basis for transactions.
		     * @param n the number of addresses
		     * @return the n generated addresses
		     * @throws InterruptedException
		     */
		    public static String[] generateUsersAddresses(int n) throws InterruptedException {
	    		Client iota = remoteDevnetNode();
	    		GetAddressesBuilder addresses = iota.getAddresses(SEED).withRange(0, n);
	    		String[] addresses_str = addresses.finish();
	    		return addresses_str;
	    		
	    	}
		    /**
		     * get the current balance of a iota address.
		     */
		    public static void getBalance(String addr) {
		        try {
		            Client iota = localNode();

		            long seed_balance = iota.getBalance(SEED).finish();
		            System.out.println("Account balance: " + seed_balance);
		            BalanceAddressResponse response = iota.getAddress().balance(addr);
		            System.out.println("The balance of " + addr + " is " + response.balance());

		            UtxoInput[] outputs = iota.getAddress().outputs(addr, new OutputsOptions());
		            System.out.println("The outputs of address " + addr + " are: " + Arrays.toString(outputs));
		        } catch (ClientException e) {
		            System.out.println("Error: " + e.getMessage());
		        }
		    }
	}
