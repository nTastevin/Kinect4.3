package org.jnect.tutorial.righthandtracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.jnect.bodymodel.Body;
import org.jnect.core.KinectManager;

public class LeftHandTracker implements Runnable {
	public static LeftHandTracker INSTANCE = new LeftHandTracker();

	PrintWriter pw;
	boolean isAlive = true;
	int i = 0;

	private LeftHandTracker() {
		File file = new File(System.getProperty("user.dir") + File.separator
				+ "data" + File.separator + "track.txt");
		try {
			pw = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};

	public void printLeftHandPosition() throws InterruptedException {
		KinectManager.INSTANCE.startKinect();
		KinectManager.INSTANCE.startSkeletonTracking();
		// System.out.println("avant boucle") ;

		float Aarbis = 400; // au delà de 360 pour etre sur que Aarbis différent
							// de AAr au départ

		while (isAlive) { // en attendant que l'on trouve une solution pour
							// changer isALive en false. A priori bouton
							// eclipse.

			Body body = KinectManager.INSTANCE.getSkeletonModel();

			float Xrh = (float) body.getRightHand().getX();
			float Yrh = (float) body.getRightHand().getY();
			float Xlh = (float) body.getLeftHand().getX();
			float Ylh = (float) body.getLeftHand().getY();
			float Xch = (float) body.getCenterHip().getX();
			float Ych = (float) body.getCenterHip().getY();
			float Xrs = (float) body.getRightShoulder().getX();
			float Yrs = (float) body.getRightShoulder().getY();
			float Xls = (float) body.getLeftShoulder().getX();
			float Yls = (float) body.getRightShoulder().getY();

			/*
			 * if (body.getHead().getX()==0){ i++;} else { i=0;} if (i==2000){
			 * isAlive=false; }
			 */

			float produitScalaire = (Xrh - Xrs) * (Xlh - Xls) + (Yrh - Yrs)
					* (Ylh - Yls);
			float normeR = (float) Math.sqrt((Xrh - Xrs) * (Xrh - Xrs)
					+ (Yrh - Yrs) * (Yrh - Yrs));
			float normeL = (float) Math.sqrt((Xlh - Xls) * (Xlh - Xls)
					+ (Ylh - Yls) * (Ylh - Yls));
			float produitNorme = normeR * normeL;
			float Aar = (float) (Math.acos(produitScalaire / produitNorme) * 180 / Math.PI);

			if (Aar != Aarbis && Ych != 0) {
				Aarbis = Aar;

				System.out.println("leftHand x: " + body.getLeftHand().getX()
						+ "|leftHand y: " + body.getLeftHand().getY()
						+ "|leftHand z: " + body.getLeftHand().getZ()
						+ "|rightHand x: " + body.getRightHand().getX()
						+ "|rightHand y: " + body.getRightHand().getY()
						+ "|rightHand z: " + body.getRightHand().getZ()
						+ "|centerHip x: " + body.getCenterHip().getX()
						+ "|centerHip y: " + body.getCenterHip().getY()
						+ "|centerHip z: " + body.getCenterHip().getZ()
						+ "|head x: " + body.getHead().getX() + "|Head y: "
						+ body.getHead().getY() + "|head z: "
						+ body.getHead().getZ() + "angle " + Aar);
				System.out.println();

				try {

					pw.append("leftHand x: " + body.getLeftHand().getX()
							+ " leftHand y: " + body.getLeftHand().getY()
							+ " leftHand z: " + body.getLeftHand().getZ()
							+ System.getProperty("line.separator")
							+ "rightHand x: " + body.getRightHand().getX()
							+ " rightHand y: " + body.getRightHand().getY()
							+ " rightHand z: " + body.getRightHand().getZ()
							+ System.getProperty("line.separator")
							+ "centerHip x: " + body.getCenterHip().getX()
							+ " centerHip y: " + body.getCenterHip().getY()
							+ " centerHip z: " + body.getCenterHip().getZ()
							+ System.getProperty("line.separator") + "head x: "
							+ body.getHead().getX() + " Head y: "
							+ body.getHead().getY() + " head z: "
							+ body.getHead().getZ()
							+ System.getProperty("line.separator") + "angle "
							+ Aar + System.getProperty("line.separator")
							+ System.getProperty("line.separator"));
					pw.flush();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					// Thread.sleep(100);
				}
			}
		}

		KinectManager.INSTANCE.stopKinect();
	}

	/*
	 * @Override public Notifier getTarget() { return body; }
	 * 
	 * @Override public void setTarget(Notifier newTarget) { // TODO
	 * Auto-generated method stub }
	 * 
	 * @Override public boolean isAdapterForType(Object type) { // TODO
	 * Auto-generated method stub return false; }
	 * 
	 * });
	 */

	public void stop() {
		isAlive = false;

	}

	@Override
	public void run() {
		try {
			printLeftHandPosition() ;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
