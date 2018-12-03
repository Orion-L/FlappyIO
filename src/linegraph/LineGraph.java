package linegraph;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

/**
 * Simple line graph drawer.
 * @author Orion
 *
 */
public class LineGraph extends JPanel {
	private static final long serialVersionUID = 8058302156874235309L;
	
	// Graph offsets into panel
	private static final int GRAPH_X_OFFSET = 50;
	private static final int GRAPH_Y_OFFSET = 50;
	
	// Axis labels and title fonts
	private static final Font LABEL_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
	private static final Font TITLE_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 20);
	
	// Starting value and increments of x-axis
	private int xLabelStart, xLabelInc;
	
	// Graph and axis titles
	private String title, xTitle, yTitle;
	
	private int[] data;  // Graph data points
	private int yMin, yMax;  // Minimum and maximum data points
	private int xEnd;  // Last value on x-axis
	
	// Graph dimensions
	private int graphWidth, graphHeight;
	private int cellWidth, cellHeight;
	private int numCellsWide, numCellsHigh;
	
	// Data-to-pixel scale
	private double xScale;
	private double yScale;
	
	/**
	 * Line graph constructor
	 * @param width Width of the panel.
	 * @param height Height of the panel.
	 * @param title Title of the graph.
	 * @param xLabelStart Starting value for the x-axis.
	 * @param xLabelInc X-axis increments of every corresponding data point.
	 * @param xTitle Title of the x-axis.
	 * @param yTitle Title of the y-axis.
	 * @param data Array of y-axis data points.
	 */
	public LineGraph(int width, int height, String title, int xLabelStart, int xLabelInc, 
			         String xTitle, String yTitle, 
			         int[] data) {
		this.title = title;
		this.xLabelStart = xLabelStart;
		this.xLabelInc = xLabelInc;
		this.xTitle = xTitle;
		this.yTitle = yTitle;
		this.data = data;

		
		// Calculate the number of cells wide the graph will be and
		// the last x-axis value.
		// If there are less than 10 data points, there will be as many
		// x-axis values and cells, otherwise there will be 10 cells and
		// the last x-axis value will be the next multiple of 10.
		if (data.length - 1 > 10) {
			this.numCellsWide = 10;
			this.xEnd = data.length + (10 - (data.length % 10));
		} else {
			this.numCellsWide = data.length - 1;
			this.xEnd = data.length - 1;
		}
		
		// Calculate the corresponding widths and x-axis scale
		this.graphWidth = width - (int)(2.5 * GRAPH_X_OFFSET);
		this.graphWidth -= this.graphWidth % (this.numCellsWide);
		this.cellWidth = this.graphWidth / this.numCellsWide;
		this.xScale = (double)this.graphWidth / this.xEnd;

		// Find the max and min data points
		this.yMax = Integer.MIN_VALUE;
		this.yMin = Integer.MAX_VALUE;
		for (int i : data) {
			if (this.yMax < i) this.yMax = i;
			if (this.yMin > i) this.yMin = i;
		}
		
		// Default max to 10 above min if all data points are the same
		if (this.yMax == this.yMin) this.yMax += 10;
		
		// Set the min to the previous multiple of 10 and max to next multiple of 10
		this.yMin = this.yMin - (this.yMin % 10);
		this.yMax = this.yMax + (10 - (this.yMax % 10));
		
		// Calculate number of cells high
		if (this.yMax - this.yMin - 1 > 10) {
			this.numCellsHigh = 10;
		} else {
			this.numCellsHigh = this.yMax - this.yMin - 1;
		}
		
		// Calculate corresponding heights and y-axis scale
		this.graphHeight = height - 3 * GRAPH_Y_OFFSET;
		this.graphHeight -= this.graphHeight % this.numCellsHigh;
		this.cellHeight = this.graphHeight / this.numCellsHigh;
		this.yScale = (double)this.graphHeight / (this.yMax - this.yMin);
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		// Draw the grid and titles
		drawGrid(g2d);
		drawTitle(g2d);

		// Loop through each data point and draw a line from the previous to the next
		g2d.setColor(Color.RED);
		int graphBase = GRAPH_Y_OFFSET + this.graphHeight;
		int prevX = 0;
		int prevY = 0;
		for (int i = 0; i < this.data.length; i++) {
			int currX = GRAPH_X_OFFSET + (int)(i * this.xScale);
			int currY = graphBase - (int)((data[i] - this.yMin) * this.yScale);
			if (i != 0) g2d.drawLine(prevX, prevY, currX, currY);
			prevX = currX;
			prevY = currY;
		}
	}
	
	@Override
	public void update(Graphics g) {
		paintComponent(g);
	}
	
	/**
	 * Draw the graph's grid and attach label.
	 * @param g2d The typecasted graphics object to draw upon.
	 */
	private void drawGrid(Graphics2D g2d) {
		int ind = 0;
		g2d.setColor(Color.BLACK);
		g2d.setFont(LABEL_FONT);
		FontMetrics fm = g2d.getFontMetrics();
		
		// Draw the vertical lines and x-axis labels
		int xInc = 1;
		if (this.xEnd > 10) xInc = this.xEnd / 10;
		for (int i = GRAPH_X_OFFSET; i <= this.graphWidth + GRAPH_X_OFFSET; i += this.cellWidth) {
			g2d.drawLine(i, GRAPH_Y_OFFSET, i, this.graphHeight + GRAPH_Y_OFFSET);
			
			String label_val = Integer.toString(this.xLabelStart + ind * xInc * this.xLabelInc);
			Rectangle2D r = fm.getStringBounds(label_val, g2d);
			g2d.drawString(label_val, (int)(i - r.getWidth() / 2), (int)(this.graphHeight + GRAPH_Y_OFFSET + r.getHeight() + 2));
						
			ind++;
		}

		// Draw the horizontal lines and y-axis labels
		ind = 0;
		int yInc = 1;
		if (this.yMax - this.yMin > 10) yInc = (this.yMax - this.yMin) / 10;
		for (int i = GRAPH_Y_OFFSET; i <= this.graphHeight + GRAPH_Y_OFFSET; i += this.cellHeight) {
			g2d.drawLine(GRAPH_X_OFFSET, i, this.graphWidth + GRAPH_X_OFFSET, i);
			
			String label_val = Integer.toString(this.yMax - ind * yInc);
			Rectangle2D r = fm.getStringBounds(label_val, g2d);
			g2d.drawString(label_val, (int)(GRAPH_X_OFFSET - r.getWidth() - 2), (int)(i + r.getHeight() / 3));
			
			ind++;
		}
	}
	
	/**
	 * Draw the graph and axis titles.
	 * @param g2d The typecasted graphics object to draw upon
	 */
	private void drawTitle(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.setFont(TITLE_FONT);
		FontMetrics fm = g2d.getFontMetrics();
		
		// Draw the title centered above the graph
		Rectangle2D r = fm.getStringBounds(title, g2d);
		g2d.drawString(title, (int)(GRAPH_X_OFFSET + this.graphWidth / 2 - r.getWidth() / 2), 
				       (int)(GRAPH_Y_OFFSET - 2));
		
		// Draw the x-axis title centered below the x-axis labels
		r = fm.getStringBounds(xTitle, g2d);
		g2d.drawString(xTitle, (int)(GRAPH_X_OFFSET + this.graphWidth / 2 - r.getWidth() / 2),
					   (int)(this.graphHeight + GRAPH_Y_OFFSET + r.getHeight() * 1.5));

		// Draw the y-axis title vertically and centered to the left of y-axis labels
		r = fm.getStringBounds(yTitle, g2d);
		AffineTransform at = g2d.getTransform();
		g2d.rotate(-Math.toRadians(90));
		g2d.translate(- r.getWidth(), 0);
		g2d.drawString(yTitle, (int)(GRAPH_X_OFFSET - this.graphHeight / 2 - r.getWidth()),
				       (int)(GRAPH_Y_OFFSET - r.getHeight() - 3));
		g2d.setTransform(at);
	}
}
