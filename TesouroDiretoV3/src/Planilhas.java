import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableWorkbook;


public class Planilhas {
	
	private Graficos graf;

	public Planilhas(){
		
	}
	
	public void baixarPlanilhas(String caminho, String url) {
		URL link;
		HttpURLConnection urlConnection;
		InputStream in;
		WritableWorkbook arquivo;
		
		try {
			link = new URL(url);
			urlConnection = (HttpURLConnection) link.openConnection();
			in = (urlConnection.getInputStream());
			
			arquivo = Workbook.createWorkbook(new File(caminho), Workbook.getWorkbook(in));
			
			arquivo.write();
			arquivo.close();

			in.close();
			urlConnection.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage());
			System.exit(0);
		}
	}

	public JPanel lerTabelas(String caminho, int[] sheets, String tipo, boolean novaJanela){
		String[][] tabela;
		String[] datas;
		float rangeMin = 0, rangeMax = 0;
		graf = new Graficos();
		
		try {
			Workbook workbook = Workbook.getWorkbook(new File(caminho));
			for(int s = 0; s < workbook.getNumberOfSheets(); s++){
				if(sheets[s] == 1){
					Sheet sheet = workbook.getSheet(s);
	
					int linhas = sheet.getRows();
					tabela = new String[2][linhas-2];
					datas = new String[linhas-2];
					
					for(int i = 0 ; i < 3; i++){
						for(int j = 2 ; j < linhas; j++){
							if(i == 0){
								datas[j-2] = sheet.getCell(i, j).getContents();
							} else {
								char[] temp = sheet.getCell(i, j).getContents().toCharArray();
								tabela[i-1][j-2] = Character.toString(temp[0]);
								for(int k = 1; k < temp.length-1; k++){
									if(temp[k] == ','){
										temp[k] = '.';
									}
									tabela[i-1][j-2] += temp[k];
								}
							}
						}
					}
					if(tipo.contains("LTN")){
						rangeMin = (float)11.5;
						rangeMax = (float)14.5;
					} else if(tipo.contains("NTNF")){
						rangeMin = (float)11.5;
						rangeMax = (float)14.5;
					} else if(tipo.contains("LFT")){
						rangeMin = (float)-1.0;
						rangeMax = (float)1.0;
					} else if(tipo.contains("NTNB")){
						rangeMin = (float)-2.0;
						rangeMax = (float)7.0;
					}
					graf.addSerie(tabela, datas, sheet.getName());
				}
			}
			
			if(novaJanela){
				graf.gerarGrafico("Taxas " + tipo, "", "taxa %", rangeMin, rangeMax);
				workbook.close();
				return new JPanel();
			} else {
				workbook.close();
				return graf.retornaGrafico("Taxas " + tipo, "", "taxa %", rangeMin, rangeMax);
			}
	        
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return new JPanel();
		}
	}
	
	public String[] getTitulos(String caminho){
		String[] titles;
		try{
			Workbook workbook = Workbook.getWorkbook(new File(caminho));
			titles = new String[workbook.getNumberOfSheets()];
			for(int s = 0; s < workbook.getNumberOfSheets(); s++){
				Sheet sheet = workbook.getSheet(s);
				titles[s] = sheet.getName();
			}
			return titles;
		}catch (Exception e){
			JOptionPane.showMessageDialog(null, e.getMessage());
			return new String[0];
		}
	}
	
	public String[] simular(String[] in){
		String[] out = new String[2];
		
		
		return out;
	}
}
