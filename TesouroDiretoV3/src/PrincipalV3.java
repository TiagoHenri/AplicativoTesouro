import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

public class PrincipalV3 extends JFrame implements ActionListener {

	/**
	 * VARIÁVEIS DE DESENVOLVIMENTO
	 */
	static boolean debug = true;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Planilhas plan;
	private static PrincipalV3 p;
	
	/*
	 * LTN = Tesouro Prefixado 20xx (T-P)
	 * NTNF = Tesouro Prefixado com juros semestrais (T-PJ)
	 * NTNB = Tesouro IPCA + com juros semestrais 20xx (T-IPCAJ)
	 * NTNB Principal = Tesouro IPCA + 20xx (T-IPCA)
	 * LFT =  Tesouro Selic (T-Selic)
	 */

	private JMenuItem atualizar;
	
	//Componentes gráficos da aba Estatística
	private JTabbedPane abas;
	private JPanel painelPrincipal, pEstatistica, pSelecao, pTitulos, pVencimentos, pGrafico;
	private JButton gerarGrafico;
	private JRadioButton taxasTP, taxasTPJ, taxasTSelic, taxasTIPCA;
	private JCheckBox[] vencimentos;
	private ButtonGroup radioButGroup;
	
	//Componentes gráficos da aba Simulação
	private JPanel pSimulacao, pSim1, pSim2, pSim1In, pSim1Out, pSim2In, pSim2Out, pSimBot;
	private JComboBox<String> opcoesSimulacao1, opcoesSimulacao2;
	private JLabel LabelDataCompra1, LabelDataVencimento1, LabelDataVenda1, LabelTaxaCompra1, LabelTaxaVenda1, LabelValorInvestido1, LabelTaxaAdm1, LabelResgate1, LabelRenta1;
	private JLabel LabelDataCompra2, LabelDataVencimento2, LabelDataVenda2, LabelTaxaCompra2, LabelTaxaVenda2, LabelValorInvestido2, LabelTaxaAdm2, LabelResgate2, LabelRenta2;
	private JFormattedTextField TextDataCompra1, TextDataVencimento1, TextDataVenda1, TextTaxaCompra1, TextTaxaVenda1, TextValorInvestido1, TextTaxaAdm1, TextResgate1, TextRenta1;
	private JFormattedTextField TextDataCompra2, TextDataVencimento2, TextDataVenda2, TextTaxaCompra2, TextTaxaVenda2, TextValorInvestido2, TextTaxaAdm2, TextResgate2, TextRenta2;
	private JButton copyToLeft, copyToRight, calcularSim1, calcularSim2;
	
	//Outras variáveis
	private static String caminhoTP = (debug ? System.getProperty("user.dir") + "/src/historico/LTN2015.xls" 
			: System.getProperty("user.dir") + "/historico/LTN2015.xls");
	private static String caminhoTPJ = (debug ? System.getProperty("user.dir") + "/src/historico/NTN-F_2015.xls" 
			: System.getProperty("user.dir") + "/historico/NTN-F_2015.xls");
	private static String caminhoTSelic = (debug ? System.getProperty("user.dir") + "/src/historico/LFT_2015.xls" 
			: System.getProperty("user.dir") + "/historico/LFT_2015.xls");
	private static String caminhoTIPCA = (debug ? System.getProperty("user.dir") + "/src/historico/NTN-B_Principal_2015.xls" 
			: System.getProperty("user.dir") + "/historico/NTN-B_Principal_2015.xls");
	
	private static String urlTP = "http://www.tesouro.fazenda.gov.br/documents/10180/137713/LTN_2015.xls";
	private static String urlTPJ = "http://www.tesouro.fazenda.gov.br/documents/10180/137713/NTN-F_2015.xls";
	private static String urlTSelic = "http://www.tesouro.fazenda.gov.br/documents/10180/137713/LFT_2015.xls";
	private static String urlTIPCA = "http://www.tesouro.fazenda.gov.br/documents/10180/137713/NTN-B_Principal_2015.xls";

	public PrincipalV3(){
		super("Tesouro Direto versão 3.0");
	}
	
	private void criaJanela(){
		setLayout(null);
		setSize(900, 700);
		setResizable(false);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menuDados = new JMenu("Dados");
		JMenu menuSobre = new JMenu("Sobre");
		menuBar.add(menuDados);
		menuBar.add(menuSobre);
		
		atualizar = new JMenuItem("Atualizar");
		atualizar.addActionListener(this);
		menuDados.add(atualizar);
		
		//criação dos painéis de simulação
		pSimulacao = new JPanel(new BorderLayout());
		pSim1 = new JPanel(new GridLayout(3, 1));
		pSim2 = new JPanel(new GridLayout(3, 1));
		pSimBot = new JPanel(new GridLayout(22, 1));
		pSimulacao.add(pSim1, BorderLayout.WEST);
		pSimulacao.add(pSimBot, BorderLayout.CENTER);
		pSimulacao.add(pSim2, BorderLayout.EAST);
		
		pSim1In = new JPanel(new GridLayout(9, 2));
		pSim1Out = new JPanel(new GridLayout(7, 2));
		pSim1.add(pSim1In);
		pSim1.add(pSim1Out);
		
		pSim2In = new JPanel(new GridLayout(9, 2));
		pSim2Out = new JPanel(new GridLayout(7, 2));
		pSim2.add(pSim2In);
		pSim2.add(pSim2Out);
		
		opcoesSimulacao1 = new JComboBox<String>(new String[]{"Selecione um título", "Tesouro pré (LTN)", "Tesouro pré com juros (NTN-F)", "Tesouro Selic (LFT)", "Tesouro IPCA (NTN-B)"});
		opcoesSimulacao1.addActionListener(this);
		pSim1In.add(opcoesSimulacao1);
		pSim1In.add(new JLabel(""));
		pSim1In.add(new JLabel(""));
		pSim1In.add(new JLabel(""));
		
		opcoesSimulacao2 = new JComboBox<String>(new String[]{"Selecione um título", "Tesouro pré (LTN)", "Tesouro pré com juros (NTN-F)", "Tesouro Selic (LFT)", "Tesouro IPCA (NTN-B)"});
		opcoesSimulacao2.addActionListener(this);
		pSim2In.add(opcoesSimulacao2);
		pSim2In.add(new JLabel(""));
		pSim2In.add(new JLabel(""));
		pSim2In.add(new JLabel(""));
		
		TextDataCompra1 = new JFormattedTextField(setMascara("##/##/####"));
		TextDataVenda1 = new JFormattedTextField(setMascara("##/##/####"));
		TextDataVencimento1 = new JFormattedTextField(setMascara("##/##/####"));
		TextTaxaAdm1 = new JFormattedTextField(setMascara("#,##%"));
		TextTaxaAdm1.setText("0,10%");
		TextTaxaCompra1 = new JFormattedTextField(setMascara("##,##%"));
		TextTaxaVenda1 = new JFormattedTextField(setMascara("##,##%"));
		TextValorInvestido1 = new JFormattedTextField(setMascara("###,##"));
		pSim1In.add(new JLabel("Data de compra: "));
		pSim1In.add(TextDataCompra1);
		pSim1In.add(new JLabel("Data de vencimento: "));
		pSim1In.add(TextDataVencimento1);
		pSim1In.add(new JLabel("Data de venda: "));
		pSim1In.add(TextDataVenda1);
		pSim1In.add(new JLabel("Valor investido (R$): "));
		pSim1In.add(TextValorInvestido1);
		pSim1In.add(new JLabel("Taxa na compra: "));
		pSim1In.add(TextTaxaCompra1);
		pSim1In.add(new JLabel("Taxa na venda: "));
		pSim1In.add(TextTaxaVenda1);
		pSim1In.add(new JLabel("Taxa da corretora: "));
		pSim1In.add(TextTaxaAdm1);
		
		TextDataCompra2 = new JFormattedTextField(setMascara("##/##/####"));
		TextDataVenda2 = new JFormattedTextField(setMascara("##/##/####"));
		TextDataVencimento2 = new JFormattedTextField(setMascara("##/##/####"));
		TextTaxaAdm2 = new JFormattedTextField(setMascara("#,##%"));
		TextTaxaAdm2.setText("0,10%");
		TextTaxaCompra2 = new JFormattedTextField(setMascara("##,##%"));
		TextTaxaVenda2 = new JFormattedTextField(setMascara("##,##%"));
		TextValorInvestido2 = new JFormattedTextField(setMascara("###,##"));
		pSim2In.add(new JLabel("Data de compra: "));
		pSim2In.add(TextDataCompra2);
		pSim2In.add(new JLabel("Data de vencimento: "));
		pSim2In.add(TextDataVencimento2);
		pSim2In.add(new JLabel("Data de venda: "));
		pSim2In.add(TextDataVenda2);
		pSim2In.add(new JLabel("Valor investido (R$): "));
		pSim2In.add(TextValorInvestido2);
		pSim2In.add(new JLabel("Taxa na compra:"));
		pSim2In.add(TextTaxaCompra2);
		pSim2In.add(new JLabel("Taxa na venda: "));
		pSim2In.add(TextTaxaVenda2);
		pSim2In.add(new JLabel("Taxa da corretora: "));
		pSim2In.add(TextTaxaAdm2);
		
		LabelResgate1 = new JLabel("Valor do resgate: ");
		LabelRenta1= new JLabel("Rentabilidade anual: ");
		TextResgate1 = new JFormattedTextField(setMascara(""));
		TextResgate1.setEditable(false);
		TextRenta1 = new JFormattedTextField(setMascara(""));
		TextRenta1.setEditable(false);
		calcularSim1 = new JButton("Calcular");
		calcularSim1.addActionListener(this);
		pSim1Out.add(calcularSim1);
		pSim1Out.add(LabelResgate1);
		pSim1Out.add(TextResgate1);
		pSim1Out.add(LabelRenta1);
		pSim1Out.add(TextRenta1);
		
		LabelResgate2 = new JLabel("Valor do resgate: ");
		LabelRenta2= new JLabel("Rentabilidade anual: ");
		TextResgate2 = new JFormattedTextField(setMascara(""));
		TextResgate2.setEditable(false);
		TextRenta2 = new JFormattedTextField(setMascara(""));
		TextRenta2.setEditable(false);
		calcularSim2 = new JButton("Calcular");
		calcularSim2.addActionListener(this);
		pSim2Out.add(calcularSim2);
		pSim2Out.add(LabelResgate2);
		pSim2Out.add(TextResgate2);
		pSim2Out.add(LabelRenta2);
		pSim2Out.add(TextRenta2);
		
		copyToLeft = new JButton("<<");
		copyToLeft.addActionListener(this);
		copyToRight = new JButton(">>");
		copyToRight.addActionListener(this);
		pSimBot.add(new JLabel());pSimBot.add(new JLabel());pSimBot.add(new JLabel());pSimBot.add(new JLabel());
		pSimBot.add(copyToRight);
		pSimBot.add(copyToLeft);
		pSimBot.add(new JLabel());pSimBot.add(new JLabel());pSimBot.add(new JLabel());pSimBot.add(new JLabel());pSimBot.add(new JLabel());
		pSimBot.add(new JLabel());pSimBot.add(new JLabel());pSimBot.add(new JLabel());pSimBot.add(new JLabel());pSimBot.add(new JLabel());
		pSimBot.add(new JLabel());pSimBot.add(new JLabel());pSimBot.add(new JLabel());pSimBot.add(new JLabel());pSimBot.add(new JLabel());
		pSimBot.add(new JLabel());
		
		
		//criação dos paineis de estatística
		painelPrincipal = new JPanel();
		painelPrincipal.setLayout(new BorderLayout());
		painelPrincipal.setSize(getWidth()-20, getHeight()-70);
		painelPrincipal.setLocation(0, 0);
		
		pEstatistica = new JPanel(new BorderLayout());
		pSelecao = new JPanel(new BorderLayout());
		pTitulos = new JPanel(new GridLayout(7, 1));
		pVencimentos = new JPanel(new GridLayout(7, 1));
		pSelecao.add(pTitulos, BorderLayout.WEST);
		pSelecao.add(pVencimentos, BorderLayout.CENTER);
		pGrafico = new JPanel();
		pEstatistica.add(pSelecao, BorderLayout.NORTH);
		pEstatistica.add(pGrafico, BorderLayout.SOUTH);
		
		abas = new JTabbedPane();
		abas.addTab("Estatísticas", null);
		abas.addTab("Simulação", null);
		abas.setComponentAt(0, pEstatistica);
		abas.setComponentAt(1, pSimulacao);
		painelPrincipal.add(abas);
		
		radioButGroup = new ButtonGroup();
		
		taxasTP = new JRadioButton("Tesouro Pré (LTN)");
		taxasTP.addActionListener(this);
		taxasTPJ = new JRadioButton("Tesouro Pré com juros (NTNF)");
		taxasTPJ.addActionListener(this);
		taxasTSelic = new JRadioButton("Tesouro Selic (LFT)");
		taxasTSelic.addActionListener(this);
		taxasTIPCA = new JRadioButton("Tesouro IPCA (NTNB)");
		taxasTIPCA.addActionListener(this);
		
		radioButGroup.add(taxasTP);
		radioButGroup.add(taxasTPJ);
		radioButGroup.add(taxasTSelic);
		radioButGroup.add(taxasTIPCA);
		
		pTitulos.add(new JLabel("TÍTULOS"));
		pTitulos.add(taxasTP);
		pTitulos.add(taxasTPJ);
		pTitulos.add(taxasTSelic);
		pTitulos.add(taxasTIPCA);
		
		gerarGrafico = new JButton("Gerar Gráfico");
		gerarGrafico.addActionListener(this);
		pTitulos.add(gerarGrafico);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object obj = e.getSource();
		
		//MENU ESTATÍSTICAS
		if (obj.equals(taxasTP)) {
			pVencimentos.removeAll();
			pVencimentos.add(new JLabel("VENCIMENTOS:"));
			String[] titles = plan.getTitulos(caminhoTP);
			vencimentos = new JCheckBox[titles.length];
			for(int i = 0; i < titles.length; i++){
				vencimentos[i] = new JCheckBox(titles[i]);
				pVencimentos.add(vencimentos[i]);
			}
			pVencimentos.setVisible(false);
			pVencimentos.setVisible(true);
			
		} else if(obj.equals(taxasTPJ)){
			pVencimentos.removeAll();
			pVencimentos.add(new JLabel("VENCIMENTOS:"));
			String[] titles = plan.getTitulos(caminhoTPJ);
			vencimentos = new JCheckBox[titles.length];
			for(int i = 0; i < titles.length; i++){
				vencimentos[i] = new JCheckBox(titles[i]);
				pVencimentos.add(vencimentos[i]);
			}
			pVencimentos.setVisible(false);
			pVencimentos.setVisible(true);
			
		} else if(obj.equals(taxasTSelic)){
			pVencimentos.removeAll();
			pVencimentos.add(new JLabel("VENCIMENTOS:"));
			String[] titles = plan.getTitulos(caminhoTSelic);
			vencimentos = new JCheckBox[titles.length];
			for(int i = 0; i < titles.length; i++){
				vencimentos[i] = new JCheckBox(titles[i]);
				pVencimentos.add(vencimentos[i]);
			}
			pVencimentos.setVisible(false);
			pVencimentos.setVisible(true);
			
		} else if(obj.equals(taxasTIPCA)){
			pVencimentos.removeAll();
			pVencimentos.add(new JLabel("VENCIMENTOS:"));
			String[] titles = plan.getTitulos(caminhoTIPCA);
			vencimentos = new JCheckBox[titles.length];
			for(int i = 0; i < titles.length; i++){
				vencimentos[i] = new JCheckBox(titles[i]);
				pVencimentos.add(vencimentos[i]);
			}
			pVencimentos.setVisible(false);
			pVencimentos.setVisible(true);
			
		} else if(obj.equals(gerarGrafico)){
			String caminho = null;
			String tipo = null;
			if(taxasTP.isSelected()){
				caminho = caminhoTP;
				tipo = "Tesouro Pré (LTN)";
			} else if(taxasTPJ.isSelected()){
				caminho = caminhoTPJ;
				tipo = "Tesouro Pré com juros (NTNF)";
			} else if(taxasTSelic.isSelected()){
				caminho = caminhoTSelic;
				tipo = "Tesouro Selic (LFT)";
			} else if(taxasTIPCA.isSelected()){
				caminho = caminhoTIPCA;
				tipo = "Tesouro IPCA (NTNB)";
			} else {
				JOptionPane.showMessageDialog(null, "Selecione um título para visualizar");
				return;
			}
			
			int sheets[] = new int[vencimentos.length];
			for(int i = 0; i < vencimentos.length; i++){
				sheets[i] = (vencimentos[i].isSelected() ? 1 : 0);
			}
			
			try{
				p.pGrafico.removeAll();
				p.pGrafico.add(plan.lerTabelas(caminho, sheets, tipo, false));
				p.pGrafico.setVisible(false);
				p.pGrafico.setVisible(true);
			}catch(Exception ex){
				JOptionPane.showMessageDialog(null, "Erro na geração do gráfico");
			}
			
		//MENU SUSPENSO	
		} else if(obj.equals(atualizar)){
			setEnabled(false);
			plan.baixarPlanilhas(caminhoTP, urlTP);
			plan.baixarPlanilhas(caminhoTPJ, urlTPJ);
			plan.baixarPlanilhas(caminhoTSelic, urlTSelic);
			plan.baixarPlanilhas(caminhoTIPCA, urlTIPCA);
			setEnabled(true);
			
		//MENU SIMULAÇÃO
		} else if(obj.equals(calcularSim1)){
			String[] in = new String[7];
			in[0] = TextDataCompra1.getText();
			in[1] = TextDataVencimento1.getText();
			in[1] = TextDataVenda1.getText();
			in[1] = TextValorInvestido1.getText();
			in[1] = TextTaxaCompra1.getText();
			in[1] = TextTaxaVenda1.getText();
			in[1] = TextTaxaAdm1.getText();
			
			String[] out = plan.simular(in);
			
			TextResgate1.setText(out[0]);
			TextRenta1.setText(out[1]);
			
		} else if(obj.equals(calcularSim2)){
			String[] in = new String[7];
			in[0] = TextDataCompra2.getText();
			in[1] = TextDataVencimento2.getText();
			in[1] = TextDataVenda2.getText();
			in[1] = TextValorInvestido2.getText();
			in[1] = TextTaxaCompra2.getText();
			in[1] = TextTaxaVenda2.getText();
			in[1] = TextTaxaAdm2.getText();
			
			String[] out = plan.simular(in);
			
			TextResgate2.setText(out[0]);
			TextRenta2.setText(out[1]);
			
		} else if(obj.equals(copyToRight)){
			opcoesSimulacao2.setSelectedIndex(opcoesSimulacao1.getSelectedIndex());
			TextDataCompra2.setText(TextDataCompra1.getText());
			TextDataVencimento2.setText(TextDataVencimento1.getText());
			TextDataVenda2.setText(TextDataVenda1.getText());
			TextValorInvestido2.setText(TextValorInvestido1.getText());
			TextTaxaCompra2.setText(TextTaxaCompra1.getText());
			TextTaxaVenda2.setText(TextTaxaVenda1.getText());
			TextTaxaAdm2.setText(TextTaxaAdm1.getText());
		} else if(obj.equals(copyToLeft)){
			opcoesSimulacao1.setSelectedIndex(opcoesSimulacao2.getSelectedIndex());
			TextDataCompra1.setText(TextDataCompra2.getText());
			TextDataVencimento1.setText(TextDataVencimento2.getText());
			TextDataVenda1.setText(TextDataVenda2.getText());
			TextValorInvestido1.setText(TextValorInvestido2.getText());
			TextTaxaCompra1.setText(TextTaxaCompra2.getText());
			TextTaxaVenda1.setText(TextTaxaVenda2.getText());
			TextTaxaAdm1.setText(TextTaxaAdm2.getText());
		}
	}
	
	private MaskFormatter setMascara(String mascara){
		MaskFormatter mask = null;
		try{
			mask = new MaskFormatter(mascara);
		}catch (Exception e){
		}
		return mask;
	}
	
	public static void main (String[]args){
		p = new PrincipalV3();
		plan = new Planilhas();
		
		p.criaJanela();
		p.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		p.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		p.add(p.painelPrincipal);
		p.setVisible(false);
		p.setVisible(true);
	}
}
