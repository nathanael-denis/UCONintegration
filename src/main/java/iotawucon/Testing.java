/*
 * Class we defines the XACML elements necessary for
 * the use case, then proceed to the basic UCON operations
 * by sending the XACML contents to the PEP and the PDP.
 */
package iotawucon;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;

import org.iota.client.Client;
import org.iota.client.Message;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.wso2.balana.Balana;
import org.wso2.balana.PDP;
import org.wso2.balana.PDPConfig;
import org.wso2.balana.ParsingException;
import org.wso2.balana.ctx.AbstractResult;
import org.wso2.balana.ctx.AttributeAssignment;
import org.wso2.balana.ctx.ResponseCtx;
import org.wso2.balana.finder.AttributeFinder;
import org.wso2.balana.finder.AttributeFinderModule;
import org.wso2.balana.finder.impl.FileBasedPolicyFinderModule;
import org.wso2.balana.xacml3.Advice;

public class Testing {
	private static String SEED = "SUPERSECURESEEDREALLY";
	private static int MIOTA = 1000000;
	private static Balana balana;
    public static String createSimpleXACMLrequest(int nbIotas) {
    	return "<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" CombinedDecision=\"false\" ReturnPolicyIdList=\"false\">\n"+
    			"<Attributes Category=\"http://iotawucon/category\">\n" +
                "<Attribute AttributeId=\"http://iotawucon/nbiotas\" IncludeInResult=\"false\">\n" +
                "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#integer\">" + nbIotas + "</AttributeValue>\n" +
                "</Attribute>\n" +
                "</Attributes>\n" +
                "</Request>";
    }
    public static String createSimpleXACMLrequest(int nbIotas, String userAddress) {
    	return "<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" CombinedDecision=\"false\" ReturnPolicyIdList=\"false\">\n"+
                "<Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\">\n" +
                "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\" IncludeInResult=\"true\">\n" +
                "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">" + userAddress +"</AttributeValue>\n" +
                "</Attribute>\n" +
                "</Attributes>\n" +
    			"<Attributes Category=\"http://iotawucon/category\">\n" +
                "<Attribute AttributeId=\"http://iotawucon/nbiotas\" IncludeInResult=\"true\">\n" +
                "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#integer\">" + nbIotas + "</AttributeValue>\n" +
                "</Attribute>\n" +
                "</Attributes>\n" +
                "</Request>";
    }
    public static String createSimpleXACMLrequest(int nbIotas, String userAddress, String dataOwnerAddress) {
    	return "<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" CombinedDecision=\"false\" ReturnPolicyIdList=\"false\">\n"+
                "<Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\">\n" +
                "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\" IncludeInResult=\"false\">\n" +
                "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">" + userAddress +"</AttributeValue>\n" +
                "</Attribute>\n" +
                "</Attributes>\n" +
                "<Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\">\n" +
                "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\" IncludeInResult=\"false\">\n" +
                "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">" + dataOwnerAddress +"</AttributeValue>\n" +
                "</Attribute>\n" +
                "</Attributes>\n" +
    			"<Attributes Category=\"http://iotawucon/category\">\n" +
                "<Attribute AttributeId=\"http://iotawucon/nbiotas\" IncludeInResult=\"false\">\n" +
                "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#integer\">" + nbIotas + "</AttributeValue>\n" +
                "</Attribute>\n" +
                "</Attributes>\n" +
                "</Request>";
    }
    
    public static String createlongXACMLrequest(int nbIotas, String userAddress, String dataOwnerAddress, int size) {
    	String s = "<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" CombinedDecision=\"false\" ReturnPolicyIdList=\"false\">\n"+
                "<Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\">\n" +
                "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\" IncludeInResult=\"false\">\n" +
                "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">" + userAddress +"</AttributeValue>\n" +
                "</Attribute>\n" +
                "</Attributes>\n" +
                "<Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\">\n" +
                "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\" IncludeInResult=\"false\">\n" +
                "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">" + dataOwnerAddress +"</AttributeValue>\n" +
                "</Attribute>\n" +
                "</Attributes>\n" +
    			"<Attributes Category=\"http://iotawucon/category\">\n";
    			for (int i =1; i< size; i++) {	
                s = s + "<Attribute AttributeId=\"http://iotawucon/nbiotas\" IncludeInResult=\"false\">\n" +
                "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#integer\">" + nbIotas + "</AttributeValue>\n" +
                "</Attribute>\n";
    			}
    	s = s + "</Attributes>\n" +
                "</Request>";
		return s;
    }
 // "<Attributes Category=\"iotawucon/Category\">\n" +
    /**
     * Initiates Balana. Needs to link with the policy database.
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public static void initBalana() throws FileNotFoundException, IOException {
    	String currentDir = System.getProperty("user.dir");
    	
     	String appConfigPath = currentDir+ "/config.properties";
     	Properties appProps = new Properties();
     	appProps.load(new FileInputStream(appConfigPath));
     	String URLPOLICY=appProps.getProperty("URLPOLICY");
     	String URLRESOURCE=appProps.getProperty("URLRESOURCE");
        // using file based policy repository. so set the policy location as system property
		String policyLocation = (new File(URLRESOURCE + "policies")) + File.separator; // to change
		System.setProperty(FileBasedPolicyFinderModule.POLICY_DIR_PROPERTY, policyLocation);
        // create default instance of Balana
        balana = Balana.getInstance();
    }
    /**
     * Same as above but returns the
     * balana object for testing.
     * @return the balana initiated
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public static Balana objinitBalana() throws FileNotFoundException, IOException {
    	String currentDir = System.getProperty("user.dir");
    	
     	String appConfigPath = currentDir+ "/config.properties";
     	Properties appProps = new Properties();
     	appProps.load(new FileInputStream(appConfigPath));
     	String URLPOLICY=appProps.getProperty("URLPOLICY");
     	String URLRESOURCE=appProps.getProperty("URLRESOURCE");
     	
        // using file based policy repository. so set the policy location as system property
		String policyLocation = (new File(URLRESOURCE + "policies/")) + File.separator; // to change
		System.setProperty(FileBasedPolicyFinderModule.POLICY_DIR_PROPERTY, policyLocation);
        // create default instance of Balana
        Balana bal = Balana.getInstance();
        return bal;
    }
    /**
     * initialisation of Finder module.
     * @return the URI of a default subject.
     * @throws URISyntaxException
     */
    public URI SampleAttributeFinderModule() throws URISyntaxException {
    	URI defaultSubjectId = new URI("urn:oasis:names:tc:xacml:1.0:subject:subject-id");
    	return defaultSubjectId;
    }
    /**
     * To get the PDP instance as defined in Balana,
     * using class PDP.
     * @return the PDP instance.
     */
    public static PDP getPDPInstance() {
        PDPConfig pdpConfig = balana.getPdpConfig();
        AttributeFinder attributeFinder = pdpConfig.getAttributeFinder();
        List<AttributeFinderModule> finderModules = attributeFinder.getModules();
        finderModules.add(new DatabaseAttributeFinderModule());
        attributeFinder.setModules(finderModules);

        return new PDP(new PDPConfig(attributeFinder, pdpConfig.getPolicyFinder(), null, true));
    }
    
    /**
     * Same as above but takes an external balana
     * as args to avoid the use of class balana.
     * @return the PDP instance.
     */
    public static PDP getPDPInstanceWoBalana(Balana balanaArg) {
        PDPConfig pdpConfig = balanaArg.getPdpConfig();
        AttributeFinder attributeFinder = pdpConfig.getAttributeFinder();
        List<AttributeFinderModule> finderModules = attributeFinder.getModules();
        finderModules.add(new DatabaseAttributeFinderModule());
        attributeFinder.setModules(finderModules);

        return new PDP(new PDPConfig(attributeFinder, pdpConfig.getPolicyFinder(), null, true));
    }
    /**
     * XACML request to DOM representation
     *
     * @param response a string XACML request
     * @return XACML request as a DOM element
     */
    public static Element getXacmlResponse(String response) {

        ByteArrayInputStream inputStream;
        DocumentBuilderFactory dbf;
        Document doc;

        inputStream = new ByteArrayInputStream(response.getBytes());
        dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        try {
            doc = dbf.newDocumentBuilder().parse(inputStream);
        } catch (Exception e) {
            System.err.println("DOM of request element can not be created from String");
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
               System.err.println("Error in closing input stream of XACML response");
            }
        }
        return doc.getDocumentElement();
    }
    
    public static void testUsageControl() throws FileNotFoundException, IOException { 
            initBalana();
    		String[] addresses = IOTAUtils.generateUsableAddress();
            String usradr = "atoi1qztx22lp5n69f4etlr6600qcdswrj8terzkga65msddc3sh4sdx02rxjqsq";
            String dataownadr = "atoi1qpty45svr0r5s9nxatzaszkde8syt4etrsmea50pmc3l2swyskc0q8wlpjs";
            String resource = "59402"; // corresponds to a key in Cassandra
            int nbiotas = 1000000;
            String s=Integer.toString(nbiotas);
            String request = createSimpleXACMLrequest(nbiotas,usradr,dataownadr);
            //String request = createXACMLRequest(usradr, dataownadr, resource, nbiotas);
            PDP pdp = getPDPInstance();

            System.out.println("XACML request");
            System.out.println(request);

            String response = pdp.evaluate(request);

            System.out.println("\n XACML response");
            System.out.println(response);

            try {
                ResponseCtx responseCtx = ResponseCtx.getInstance(getXacmlResponse(response));
                AbstractResult result  = responseCtx.getResults().iterator().next();
                if(AbstractResult.DECISION_PERMIT == result.getDecision()){
                	System.out.println("\n User with address " + usradr + " paid " + Integer.toString(nbiotas) + " iotas to user " + dataownadr );
                    System.out.println("\n User with address " + usradr + " is authorized to access this data\n\n");
                } else {
                    System.out.println("\n User with address " + usradr + " is NOT authorized to access this data\n");
                    List<Advice> advices = result.getAdvices();
                    for(Advice advice : advices){
                        List<AttributeAssignment> assignments = advice.getAssignments();
                        for(AttributeAssignment assignment : assignments){
                            System.out.println("Advice :  " + assignment.getContent() +"\n\n");
                        }
                    }
                }
            } catch (ParsingException e) {
                e.printStackTrace();
            }

        }
    
    /**
     * testing for the time to evaluate policy
     * @param samples the number of evaluations
     * @param policyLength the size of the policy in 
     * number of rules.
     * @throws IOException 
     */
    public static void policyEvaluationTime(int samples, int policyLength) throws IOException { 
        initBalana();
    	String currentDir = System.getProperty("user.dir");
    	
     	String appConfigPath = currentDir+ "/config.properties";
     	Properties appProps = new Properties();
     	appProps.load(new FileInputStream(appConfigPath));
     	String URLPOLICY=appProps.getProperty("URLPOLICY");
     	String URLRESOURCE=appProps.getProperty("URLRESOURCE");
        List<Long> policyTimesList =new ArrayList<Long>();
		String[] addresses = IOTAUtils.generateUsableAddress();
        String usradr = "atoi1qztx22lp5n69f4etlr6600qcdswrj8terzkga65msddc3sh4sdx02rxjqsq";
        String dataownadr = "atoi1qpty45svr0r5s9nxatzaszkde8syt4etrsmea50pmc3l2swyskc0q8wlpjs";
        String resource = "59402"; // corresponds to a key in Cassandra
        int nbiotas = 1000000;
        String s=Integer.toString(nbiotas);
        String request = createlongXACMLrequest(nbiotas,usradr,dataownadr,policyLength);
        //String request = createXACMLRequest(usradr, dataownadr, resource, nbiotas);
        PDP pdp = getPDPInstance();
        for (int i =1; i< samples; i++) {
        long start_policy = System.currentTimeMillis();
        String response = pdp.evaluate(request);
    	long finish_policy = System.currentTimeMillis();
        System.out.println("\n XACML response");
        System.out.println(response);
    	long timeElapsed_policy = finish_policy - start_policy;
    	policyTimesList.add(timeElapsed_policy);
    	System.setOut(new PrintStream(new FileOutputStream(URLRESOURCE+ "results/policy_check.csv")));
    	policyTimesList.forEach( fetch -> { System.out.println(fetch);});

    	}
    }
    /**
     * Control the access to a data for one user.
     * @param userAddress
     * @param dataOwnerAddress
     * @param nbIotas
     * @return true if the access is successful.
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public static Boolean AccessControlOneUser(String userAddress, String dataOwnerAddress,
    		int nbIotas) throws FileNotFoundException, IOException { 
    	Boolean boolResult = false;
        initBalana();
        String request = createSimpleXACMLrequest(nbIotas,userAddress,dataOwnerAddress);
        PDP pdp = getPDPInstance();

        System.out.println("XACML request");
        System.out.println(request);

        String response = pdp.evaluate(request);

        System.out.println("\n XACML response");
        System.out.println(response);

        try {
            ResponseCtx responseCtx = ResponseCtx.getInstance(getXacmlResponse(response));
            AbstractResult result  = responseCtx.getResults().iterator().next();
            if(AbstractResult.DECISION_PERMIT == result.getDecision()){
            	System.out.println("\n User with address " + userAddress + " paid " + Integer.toString(nbIotas) + " iotas to user " + dataOwnerAddress );
                System.out.println("\n User with address " + userAddress + " is authorized to access this data\n\n");
                boolResult = true;
            } else {
                System.out.println("\n User with address " + userAddress + " is NOT authorized to access this data\n");
                List<Advice> advices = result.getAdvices();
                for(Advice advice : advices){
                    List<AttributeAssignment> assignments = advice.getAssignments();
                    for(AttributeAssignment assignment : assignments){
                        System.out.println("Advice :  " + assignment.getContent() +"\n\n");
                    }
                }
            }
        } catch (ParsingException e) {
            e.printStackTrace();
        }
        System.out.println(boolResult);
        return boolResult;

    }
    /**
     * unfold the full chain of access control, which is
     * 1)the buyer pays for a transaction;
     * 2)our UCS emits the transaction either a) as the node or 
     * b) relying on the devnet load balancer.
     * 3)the UCS generate the request accordingly to the transaction outputs;
     * 3bis) the UCS generate the XACML politic accordingly.
     * 4)The UCS send the request to its PEP for evaluation;
     * 5)We return the decision.
     * @param brokerAddress
     * @param nbIotas (should be 1Mio on testnet)
     * @throws Exception xml parser
     */
    public static Boolean eventChainAccessControl(String brokerAddress, int nbIotas) throws Exception {
    	//TRANSACTION
    	String currentDir = System.getProperty("user.dir");
    	
     	String appConfigPath = currentDir+ "/config.properties";
     	Properties appProps = new Properties();
     	appProps.load(new FileInputStream(appConfigPath));
     	String URLPOLICY=appProps.getProperty("URLPOLICY");
    	long start_transaction = System.currentTimeMillis();
    	String[] outputs = IOTAUtils.transaction(SEED, brokerAddress, nbIotas, "REMOTE");
    	long finish_transaction = System.currentTimeMillis();
    	long timeElapsed_transaction = finish_transaction - start_transaction;
    	String[] changes = XACMLutils.findAttributeValue();
    	// POLICY
    	long start_policy = System.currentTimeMillis(); // note that generating the policy is for convenience, not compulsory
    	XACMLutils.changeStringXml(URLPOLICY, changes[1], brokerAddress);
    	XACMLutils.changeStringXml(URLPOLICY, changes[0], outputs[0]);
    	XACMLutils.changeStringXml(URLPOLICY, changes[2] , String.valueOf(nbIotas));
    	long finish_policy = System.currentTimeMillis();
    	long timeElapsed_policy = finish_policy - start_policy;
    	//ACCESS
    	long start_access = System.currentTimeMillis();
    	Boolean result = AccessControlOneUser(outputs[0], brokerAddress, Integer.parseInt(outputs[2]));
    	long finish_access = System.currentTimeMillis();
    	long timeElapsed_access = finish_access - start_access;
    	System.out.println("Time for transaction : " + timeElapsed_transaction);
    	System.out.println("Time for policy (operation not compulsory) : " + timeElapsed_policy);
    	System.out.println("Time for access : " + timeElapsed_access);
    	//RESULT
    	return result;
    }
    /**
     * The function triggers an event chain for several users, either connecting
     * on the remote devnet node or the local devnet node. The purpose
     * is to determine if there is a difference in performance when using different settings,
     * local (integrated) or remote. 
     * @param nbIotas the number of iotas in the transaction.
     * @param nbUsers the number of users requiring access.
     * @param node either "REMOTE" or "LOCALHOST" string.
     * @return a list of boolean for the access results (should be all true).
     * @throws Exception
     */
    public static List<Boolean> transactionTime(int nbIotas, int nbUsers, String node) throws Exception {
    	//STORAGE FOR RESULTS
    	String currentDir = System.getProperty("user.dir");
    	
     	String appConfigPath = currentDir+ "/config.properties";
     	Properties appProps = new Properties();
     	appProps.load(new FileInputStream(appConfigPath));
     	String URLPOLICY=appProps.getProperty("URLPOLICY");
     	String URLRESOURCE=appProps.getProperty("URLRESOURCE");
    	List<Long> transactionTimesList =new ArrayList<Long>();
    	List<Long> policiesTimesList =new ArrayList<Long>();
    	List<Long> accessesTimesList =new ArrayList<Long>();
    	List<Boolean> results = new ArrayList<Boolean>();
    	//PICK A NODE FOR THE TEST
    	Client iota = null;
    	if (node == "REMOTE") {
    		//iota = IOTAUtils.remoteDevnetNode();
    		iota = IOTAUtils.remoteNode();
    	}
    	if (node == "LOCALHOST") {
    		iota = IOTAUtils.localNode();
    	}
    	//GENERATE THE ADDRESSES WHICH WILL BE USED FOR ACCESS
    	String[] addresses = iota.getAddresses(SEED).withRange(0, nbUsers).finish();
    	// START THE WHOLE EVENT CHAIN FOR EACH USER
    	for (int i = 1; i < nbUsers; i++){
    	
    	String[] outputs = new String[4];
    	long start_transaction = System.currentTimeMillis();
    	if (node == "LOCALHOST") {
    		outputs = IOTAUtils.transaction(SEED, addresses[i], nbIotas, "LOCALHOST"); 
    	}
    	if (node == "REMOTE") {
    		outputs = IOTAUtils.transaction(SEED, addresses[i], nbIotas, "REMOTE");
    	}
    	long finish_transaction = System.currentTimeMillis();
    	long timeElapsed_transaction = finish_transaction - start_transaction;
    	transactionTimesList.add(timeElapsed_transaction);
    	//POLICIES
    	long start_policy = System.currentTimeMillis(); // note that generating the policy is for convenience, not compulsory
    	String[] changes = XACMLutils.findAttributeValue();
    	XACMLutils.changeStringXml(URLPOLICY, changes[1], addresses[i]);
    	XACMLutils.changeStringXml(URLPOLICY, changes[0], outputs[0]);
    	XACMLutils.changeStringXml(URLPOLICY, changes[2] , String.valueOf(nbIotas));
    	long finish_policy = System.currentTimeMillis();
    	long timeElapsed_policy = finish_policy - start_policy;
    	policiesTimesList.add(timeElapsed_policy);
    	//ACCESSES
    	long start_access = System.currentTimeMillis();
    	Boolean result = AccessControlOneUser(outputs[0], addresses[i], Integer.parseInt(outputs[2]));
    	long finish_access = System.currentTimeMillis();
    	long timeElapsed_access = finish_access - start_access;
    	accessesTimesList.add(timeElapsed_access);
    	results.add(result);
    	//System.out.println("Time for transaction" + i + " : " + timeElapsed_transaction);
    	//System.out.println("Time for policy (operation not compulsory) : " + timeElapsed_policy);
    	//System.out.println("Time for access : " + timeElapsed_access);
    	// WRITING THE RESULTS
    	if (node == "REMOTE") {
    	System.setOut(new PrintStream(new FileOutputStream(URLRESOURCE + "results/transaction_times_remote.csv")));
    	transactionTimesList.forEach( time -> { System.out.println(time);});
    	System.setOut(new PrintStream(new FileOutputStream(URLRESOURCE + "results/policy_times_remote.csv")));
    	policiesTimesList.forEach( policy -> { System.out.println(policy);});
    	System.setOut(new PrintStream(new FileOutputStream(URLRESOURCE + "results/access_times_remote.csv")));
    	accessesTimesList.forEach( access -> { System.out.println(access);});
    	}
       	if (node == "LOCALHOST") {
        	System.setOut(new PrintStream(new FileOutputStream(URLRESOURCE + "results/transaction_times_local.csv")));
        	transactionTimesList.forEach( time -> { System.out.println(time);});
        	System.setOut(new PrintStream(new FileOutputStream(URLRESOURCE + "results/policy_times_local.csv")));
        	policiesTimesList.forEach( policy -> { System.out.println(policy);});
        	System.setOut(new PrintStream(new FileOutputStream(URLRESOURCE + "results/access_times_local.csv")));
        	accessesTimesList.forEach( access -> { System.out.println(access);});
        }
    	}
    	//RESULTS
    	return results;
    }
    /**
     * Corresponds to fetchTransaction in the sequence diagram.
     * @param nbUsers
     * @param nbIotas
     * @throws IOException 
     */
    public static void remoteTransactionsOverhead(int nbUsers, int nbIotas) throws IOException {
    	/*Operation relevant only on remote node, on localhost, results are kept
    	 * locally (in the UCS code) and fetch time is around 1ms when measured.
    	*/
    	String currentDir = System.getProperty("user.dir");
    	
     	String appConfigPath = currentDir+ "/config.properties";
     	Properties appProps = new Properties();
     	appProps.load(new FileInputStream(appConfigPath));
     	String URLPOLICY=appProps.getProperty("URLPOLICY");
     	String URLRESOURCE=appProps.getProperty("URLRESOURCE");
    	List<Long> fetchTimesList =new ArrayList<Long>();
    	Client iota = IOTAUtils.remoteNode();
    	String[] addresses = iota.getAddresses(SEED).withRange(0, nbUsers).finish();
    	for (int i =1; i< nbUsers; i++) {
            Message message = iota
                    .message()
                    .withSeed(SEED)
                    .finish();
            //Fetch measures 
            long start_fetch = System.currentTimeMillis();
            /* This is the only operation creating a overhead
            // but, it is due to the separation of
            // the fetching and the processing. The
             * processing cost is actually negligible.
            */
    		Message fetch = iota.getMessage().data(message.id());
    		long stop_fetch = System.currentTimeMillis();
    		long timeElapsed_fetch = stop_fetch - start_fetch;
    		fetchTimesList.add(timeElapsed_fetch);
    		//Query process measures
    		long start_process = System.currentTimeMillis();
    		long stop_process = System.currentTimeMillis();
    	}
    	System.setOut(new PrintStream(new FileOutputStream(URLRESOURCE+ "results/fetch_times_remote.csv")));
    	fetchTimesList.forEach( fetch -> { System.out.println(fetch);});
    }

    
    }
