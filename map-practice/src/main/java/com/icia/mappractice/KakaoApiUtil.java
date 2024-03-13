package com.icia.mappractice;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class KakaoApiUtil {
	private static final String REST_API_KEY = "67dcd085131ee1aa76ea4ba836a92418";

	public static Point getPointByAddress() {
		
		
		
		return null;
	}
	
	public static List<KeywordMaker> getPointByKeyword(String keyword) throws IOException, InterruptedException {
		
		return null;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class KakaoAddress {
		private List<Document> documents;

		public List<Document> getDocuments() {
			return documents;
		}

		@JsonIgnoreProperties(ignoreUnknown = true)
		public static class Document {
			Double x;
			Double y;

			public Double getX() {
				return x;
			}

			public Double getY() {
				return y;
			}
		}
	}

	public static class Point {
		private Double x;
		private Double y;

		public Point(Double x, Double y) {
			this.x = x;
			this.y = y;
		}

		public Double getX() {
			return x;
		}

		public Double getY() {
			return y;
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class KakaoDirections {
		private List<Route> routes;

		public List<Route> getRoutes() {
			return routes;
		};

		@JsonIgnoreProperties(ignoreUnknown = true)
		public static class Route {
			private List<Section> sections;

			public List<Section> getSections() {
				return sections;
			}

			@JsonIgnoreProperties(ignoreUnknown = true)
			public static class Section {
				private List<Road> roads;

				public List<Road> getRoads() {
					return roads;
				}

				@JsonIgnoreProperties(ignoreUnknown = true)
				public static class Road {
					private List<Double> vertexes;

					public List<Double> getVertexes() {
						return vertexes;
					}
				}
			}
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class KeywordMaker {
		private Double x;
		private Double y;
		private String name;
		private String tel;

		public KeywordMaker(Double x, Double y, String name, String tel, int id, String url) {
			this.x = x;
			this.y = y;
			this.name = name;
			this.tel = tel;
			this.id = id;
		}

		private int id;

		public int getId() {
			return id;
		}

		public Double getX() {
			return x;
		}

		public Double getY() {
			return y;
		}

		public String getName() {
			return name;
		}

		public String getTel() {
			return tel;
		}
	}
}
