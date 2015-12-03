package timer;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TimeFrame extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel time = new JLabel("Time goes here", JLabel.CENTER);
	
	private JFrame pausePopup = new JFrame();
	private Timer timer;
	private long minutes;
	private long seconds;
	
	public TimeFrame(){
		timer = new Timer(this);
		add(time);
		
		this.setOpaque(false);
		
		/*JLabel timeIcon = new JLabel();
		ImageIcon timeImg = new ImageIcon("src/swampimages/time.png");
		timeIcon.setIcon(timeImg);
		Meter timeMeter = new Meter(35,50, 100, 20);
		add(timeIcon);
		add(timeMeter, BorderLayout.WEST);*/
		
		timer.startTimer();
	}
	public void update(long dT){
		// convert milliseconds into other forms
		this.minutes = (dT/60000)%1000;
		this.seconds = (dT/1000)%60;
		
		time.setText(String.valueOf((dT/60000)%1000) + ":" + String.valueOf((dT/1000)%60));
		time.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		
		
		
		
	}
	
	public JFrame getPausePopup() {
		return pausePopup;
	}
	public void setPausePopup(JFrame pausePopup) {
		this.pausePopup = pausePopup;
	}
	public Timer getTimer() {
		return timer;
	}
	public void setTimer(Timer timer) {
		this.timer = timer;
	}
	public long getMinutes() {
		return minutes;
	}
	public void setMinutes(long minutes) {
		this.minutes = minutes;
	}
	public long getSeconds() {
		return seconds;
	}
	public void setSeconds(long seconds) {
		this.seconds = seconds;
	}
}