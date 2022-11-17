package iotawucon;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Main module
 *
 */
public class App {
	/**
	 * @param args any arguments to be given
	 * @throws Exception 
	 */
	private static final String BUYERADDRESS="atoi1qrkzqqfy6t6qm69mh9es6s4rauv3evvztjesntty7d7kzz2pwqdywcmqdkg";
	private static final String GENESIS="atoi1qpszqzadsym6wpppd6z037dvlejmjuke7s24hm95s9fg9vpua7vluehe53e";
	private static final String SEED="SUPERSECURESEEDREALLY";

    public static void main(String[] args) throws Exception {
    	//IOTA Java library
		System.loadLibrary("iota_client");
		IOTAUtils.getTipsFromDevNode();
		
		// BEGINNING OF PERFORMANCE TESTS
		
		//IOTAUtils.homeMadeFaucet(251);
		//IOTAUtils.getBalance(GENESIS);
		//IOTAUtils.getBalance(BUYERADDRESS);
		
		/**
		 * All tests below
		 * Adjust the configuration file beforehand to 
		 * write results in the right place and interact
		 * with your AWS node for the remote setting
		 */
		
		//Policies evaluation time
		Testing.policyEvaluationTime(1500,1000);

		// Transaction time remote and local, example for 5 samples
		//Testing.transactionTime(1000000, 5, "REMOTE");
		Testing.transactionTime(1000000,5,"LOCALHOST");
		
		// Overhead due to communication, remote setting
		//SampleDataEnergy.remoteTransactionsOverhead(1501, MIOTA);
    	}
}