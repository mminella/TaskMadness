/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.marchmadness;

/**
 * @author Glenn Renfro
 */
public class NcaaStats {
	private int year;
	private int rank;
	private String name;
	private double rating;
	private int win;
	private int loss;
	private double schedl;
	private int schedlRank;
	private int winTop25;
	private int lossTop25;
	private int winTop50;
	private int lossTop50;
	private double predictor;
	private int predictorRank;
	private double goldenMean;
	private int goldenMeanRank;
	private double recent;
	private int recentRank;

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public int getWin() {
		return win;
	}

	public void setWin(int win) {
		this.win = win;
	}

	public int getLoss() {
		return loss;
	}

	public void setLoss(int loss) {
		this.loss = loss;
	}

	public double getSchedl() {
		return schedl;
	}

	public void setSchedl(double schedl) {
		this.schedl = schedl;
	}

	public int getSchedlRank() {
		return schedlRank;
	}

	public void setSchedlRank(int schedlRank) {
		this.schedlRank = schedlRank;
	}

	public int getWinTop25() {
		return winTop25;
	}

	public void setWinTop25(int winTop25) {
		this.winTop25 = winTop25;
	}

	public int getLossTop25() {
		return lossTop25;
	}

	public void setLossTop25(int lossTop25) {
		this.lossTop25 = lossTop25;
	}

	public int getWinTop50() {
		return winTop50;
	}

	public void setWinTop50(int winTop50) {
		this.winTop50 = winTop50;
	}

	public int getLossTop50() {
		return lossTop50;
	}

	public void setLossTop50(int lossTop50) {
		this.lossTop50 = lossTop50;
	}

	public double getPredictor() {
		return predictor;
	}

	public void setPredictor(double predictor) {
		this.predictor = predictor;
	}

	public int getPredictorRank() {
		return predictorRank;
	}

	public void setPredictorRank(int predictorRank) {
		this.predictorRank = predictorRank;
	}

	public double getGoldenMean() {
		return goldenMean;
	}

	public void setGoldenMean(double goldenMean) {
		this.goldenMean = goldenMean;
	}

	public int getGoldenMeanRank() {
		return goldenMeanRank;
	}

	public void setGoldenMeanRank(int goldenMeanRank) {
		this.goldenMeanRank = goldenMeanRank;
	}

	public double getRecent() {
		return recent;
	}

	public void setRecent(double recent) {
		this.recent = recent;
	}

	public int getRecentRank() {
		return recentRank;
	}

	public void setRecentRank(int recentRank) {
		this.recentRank = recentRank;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return "NcaaStats{" +
				"year=" + year +
				", rank=" + rank +
				", name='" + name + '\'' +
				", rating=" + rating +
				", win=" + win +
				", loss=" + loss +
				", schedl=" + schedl +
				", schedlRank=" + schedlRank +
				", winTop25=" + winTop25 +
				", lossTop25=" + lossTop25 +
				", winTop50=" + winTop50 +
				", lossTop50=" + lossTop50 +
				", predictor=" + predictor +
				", predictorRank=" + predictorRank +
				", goldenMean=" + goldenMean +
				", goldenMeanRank=" + goldenMeanRank +
				", recent=" + recent +
				", recentRank=" + recentRank +
				'}';
	}
}
