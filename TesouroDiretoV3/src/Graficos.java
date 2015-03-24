import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Graficos {
	
	private DefaultCategoryDataset taxa;

	public Graficos(){
		taxa = new DefaultCategoryDataset();
	}
	
	public void addSerie(String[][] tabela, String[] datas, String name){
		
		for(int i = 0; i < tabela.length; i++){
			for(int j = 0; j < tabela[0].length; j++){
				if(i == 0){
					taxa.addValue(Double.parseDouble(tabela[i][j]), "compra " + name, datas[j]);
				} else {
//					taxa.addValue(Double.parseDouble(tabela[i][j]), "venda " + name, datas[j]);
				}
			}
		}
	}
	
	public void gerarGrafico(String title, String legX, String legY, float rangeMin, float rangeMax){
		JFreeChart graf = ChartFactory.createLineChart(title, legX, legY, taxa, PlotOrientation.VERTICAL, true, true, false);
		CategoryPlot cp = graf.getCategoryPlot();
		cp.getRangeAxis().setRange(rangeMin, rangeMax);
		cp.getRangeAxis().setAutoRange(false);
		cp.getRenderer().setSeriesPaint(0, Color.BLUE);
		
		ChartFrame myChartFrame = new ChartFrame("Análise gráfica", graf);
        myChartFrame.pack();
        myChartFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        myChartFrame.setVisible(true);
	}
	
	public JPanel retornaGrafico(String title, String legX, String legY, float rangeMin, float rangeMax){
		JFreeChart graf = ChartFactory.createLineChart(title, legX, legY, taxa, PlotOrientation.VERTICAL, true, true, false);
		graf.setBackgroundPaint(new Color(238, 238, 238));
		CategoryPlot cp = graf.getCategoryPlot();
		cp.getRangeAxis().setRange(rangeMin, rangeMax);
		cp.getRangeAxis().setAutoRange(false);
		cp.getRenderer().setSeriesPaint(0, Color.BLUE);
		
		ChartPanel panel = new ChartPanel(graf);
		
		return panel;
	}
}
