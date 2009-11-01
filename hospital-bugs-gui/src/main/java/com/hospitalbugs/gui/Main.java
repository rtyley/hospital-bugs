package com.hospitalbugs.gui;

import static java.lang.Math.log1p;
import static java.lang.Math.round;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import static org.joda.time.Duration.standardDays;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.format.ISODateTimeFormat;

import com.hospitalbugs.analysis.ConstantTransportFunction;
import com.hospitalbugs.analysis.HospitalMicrobialLoad;
import com.hospitalbugs.analysis.HospitalMicrobialLoadCalculationFactory;
import com.hospitalbugs.analysis.PatientMicrobialLoadExposure;
import com.hospitalbugs.analysis.RandomHospitalInfectionDataFactory;
import com.hospitalbugs.io.InfectionCSVLineParser;
import com.hospitalbugs.io.PatientWardStayCSVLineParser;
import com.hospitalbugs.model.HospitalInfectionDonorOccupancy;
import com.hospitalbugs.model.Infection;
import com.hospitalbugs.model.PatientFactory;
import com.hospitalbugs.model.StandardisedMicrobialLoad;
import com.hospitalbugs.model.WardFactory;
import com.madgag.io.csv.CSVFileParser;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;


public class Main {
	public static class InfectionExposure {

		private final float value;
		private final float magnitude;
		private final String text;

		public InfectionExposure(float value) {
			this.value = value;
			magnitude = 1f-(float)log1p(value/2);
			text = NumberFormat.getInstance().format(value);
		}
		
		public float getMagnitude() {
			return magnitude;
		}
		
		public float getValue() {
			return value;
		}

		@Override
		public String toString() {
			return text;
		}
	}
	public static void main(String[] args) throws FileNotFoundException, IOException {
		CSVFileParser csvFileParser = new CSVFileParser();
		DateTimeZone dateTimeZone = DateTimeZone.forID("Europe/London");
		PatientFactory patientFactory = new PatientFactory();
		WardFactory wardFactory = new WardFactory();
		
		csvFileParser.parse(new FileReader(args[0]), new PatientWardStayCSVLineParser(patientFactory, wardFactory, dateTimeZone));
		List<Infection> infections = csvFileParser.parse(new FileReader(args[1]), new InfectionCSVLineParser(patientFactory, dateTimeZone));
		
		HospitalInfectionDonorOccupancy donorOccupancy = new HospitalInfectionDonorOccupancy(infections);
		
		HospitalMicrobialLoad hospitalMicrobialLoad =
			new HospitalMicrobialLoadCalculationFactory().lambdaModel(donorOccupancy, 0.7f, new ConstantTransportFunction(0.2f));
		
		System.out.println(hospitalMicrobialLoad);
		PatientMicrobialLoadExposure exposure = new PatientMicrobialLoadExposure();
		for (Infection infection : infections) {
			StandardisedMicrobialLoad microbialLoad = exposure.microbialLoadExposureDuringPatientSusceptibilityFor(infection, hospitalMicrobialLoad);
			System.out.println(infection+" - "+microbialLoad);
		}
		
        display(infections, hospitalMicrobialLoad);
        
        doBigRandom();
    }
	private static void doBigRandom() {
		Interval fullTimeSpan = new Interval(new Instant(), standardDays(30));
		List<Infection> infections = RandomHospitalInfectionDataFactory.generateInfections(fullTimeSpan, 30, 10);
		HospitalInfectionDonorOccupancy donorOccupancy = new HospitalInfectionDonorOccupancy(infections);
		HospitalMicrobialLoad hospitalMicrobialLoad =
			new HospitalMicrobialLoadCalculationFactory().lambdaModel(donorOccupancy, 0.7f, new ConstantTransportFunction(0.2f));
		display(infections, hospitalMicrobialLoad);
		
	}
	private static void display(List<Infection> infections,
			HospitalMicrobialLoad hospitalMicrobialLoad) {
		Graph<Infection, InfectionExposure> infectionGraph = getGraph(infections, hospitalMicrobialLoad);
        show(infectionGraph);
	}
	private static void show(Graph<Infection, InfectionExposure> infectionGraph) {
		//Layout<Infection, Float> layout = new FRLayout<Infection, Float>(g);
		
		Layout<Infection, InfectionExposure> layout = new SpringLayout2<Infection, InfectionExposure>(infectionGraph,new Transformer<InfectionExposure, Integer>() {
			@Override
			public Integer transform(InfectionExposure input) {
				return round(100f*(0.2f+input.getMagnitude()));
			}
		});
		VisualizationViewer<Infection, InfectionExposure> vv = new VisualizationViewer<Infection, InfectionExposure>(layout);
		vv.setVertexToolTipTransformer(new Transformer<Infection, String>() {
			@Override
			public String transform(Infection infection) {
				return infection.getPatient() + " : " + ISODateTimeFormat.date().print(infection.getTransition());
			}
		});
		vv.setEdgeToolTipTransformer(new Transformer<InfectionExposure, String>() {
			@Override
			public String transform(InfectionExposure infection) {
				return infection.toString();
			}
		});
		
		Transformer<InfectionExposure, Paint> loadToPaintTransformer = new Transformer<InfectionExposure, Paint>() {
			
			@Override
			public Paint transform(InfectionExposure input) {
				float intensity = input.getMagnitude();
				return new Color(intensity, intensity, intensity,1-intensity);
			}
		};
		RenderContext<Infection, InfectionExposure> renderContext = vv.getRenderContext();
		DefaultModalGraphMouse<Infection, Float> gm = new DefaultModalGraphMouse<Infection,Float>();
		gm.setMode(ModalGraphMouse.Mode.PICKING);
		vv.setGraphMouse(gm);

		renderContext.setEdgeIncludePredicate(new Predicate<Context<Graph<Infection,InfectionExposure>,InfectionExposure>>() {
			@Override
			public boolean evaluate(Context<Graph<Infection, InfectionExposure>, InfectionExposure> load) {
				return load.element.getValue() > 0.1;
			}
		});
		renderContext.setArrowFillPaintTransformer(loadToPaintTransformer);
		renderContext.setArrowDrawPaintTransformer(loadToPaintTransformer);
		renderContext.setEdgeDrawPaintTransformer(loadToPaintTransformer);
		renderContext.setEdgeStrokeTransformer(new Transformer<InfectionExposure, Stroke>() {
			@Override
			public Stroke transform(InfectionExposure input) {
				return new BasicStroke(5*(1-input.getMagnitude()));
			}
		});
		
        JFrame jf = new JFrame();
        jf.getContentPane().add(vv);
        jf.setDefaultCloseOperation(EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
	}

    public static Graph<Infection, InfectionExposure> getGraph(List<Infection> infections, HospitalMicrobialLoad hospitalMicrobialLoad)
    {
    	
    	Graph<Infection, InfectionExposure> g = new DirectedSparseGraph<Infection, InfectionExposure>();
    	PatientMicrobialLoadExposure exposure = new PatientMicrobialLoadExposure();
    	for (Infection infection : infections) {
    		g.addVertex(infection);
		}
    	for (Infection infection : infections) {
    		StandardisedMicrobialLoad microbialLoad = exposure.microbialLoadExposureDuringPatientSusceptibilityFor(infection, hospitalMicrobialLoad);
    		for (Map.Entry<Infection, Float> entry : microbialLoad.getLoad().entrySet()) {
    			float load = entry.getValue();
    			if (load>0.1) {
    			Infection donorInfection = entry.getKey();
				InfectionExposure ie = new InfectionExposure(load);
				g.addEdge(ie, donorInfection,infection);
    			}
    		}
		}
        return g;
    }
}
