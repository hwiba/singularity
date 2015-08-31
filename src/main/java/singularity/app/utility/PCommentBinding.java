package singularity.app.utility;

public class PCommentBinding {

//	public static List<Comment> UpdatePId(String[] originParagraph, String[] editedParagraph,
//			List<Comment> pComments) {
//
//		double minSimilarity = ApplicationCofigure.PCOMMENT_MIN_SIMILARITY_RATE;
//		List<String> wordDic = makeDic(originParagraph, editedParagraph, pComments);
//
//		// 문단별 벡터 배열 만들기
//		int[][] originParagraphWrodVector = new int[originParagraph.length][];
//		int[][] editedParagraphWrodVector = new int[editedParagraph.length][];
//
//		for (int i = 0; i < originParagraph.length; i++) {
//			String[] originWords = splitWord(originParagraph[i]);
//			originParagraphWrodVector[i] = getVector(wordDic, originWords);
//		}
//		for (int i = 0; i < editedParagraph.length; i++) {
//			String[] editedWords = splitWord(editedParagraph[i]);
//			editedParagraphWrodVector[i] = getVector(wordDic, editedWords);
//		}
//
//		for (Comment pComment : pComments) {
//			Map<Integer, Double> score = new HashMap<Integer, Double>();
//			int pId = (int) pComment.getPId();
//			if(pId == -1){
//				continue;
//			}
//			for (int k = 0; k < editedParagraph.length; k++) {
//				score.put(k, cosineSimilarity(editedParagraphWrodVector[k], originParagraphWrodVector[pId - 1]));
//			}
//			Iterator<Integer> it = sortByValue(score).iterator();
//			int key = it.next();
//
//			if (score.get(key) < minSimilarity) {
//				pComment.setPId(-1);;
//			} else {
//				pComment.setPId(key + 1);
//			}
//		}
//		return pComments;
//	}
//
//	private static String[] splitWord(String text) {
//		return text.split("( )+|( )*(\r\n)+");
//	}
//
//	private static int[] getVector(List<String> wordDic, String[] pWord) {
//		int index;
//		int[] vector = new int[wordDic.size()];
//		for (String word : pWord) {
//			index = wordDic.indexOf(word);
//			if (index != -1) {
//				vector[wordDic.indexOf(word)]++;
//			}
//		}
//		return vector;
//	}
//
//	private static List<String> makeDic(String[] originParagraph, String[] editedParagraph,
//			List<Comment> pComments) {
//		Set<String> wordSet = new TreeSet<String>();
//
//		String allSentence = "";
//		for (String sen : originParagraph) {
//			allSentence += sen + " ";
//		}
//		for (String sen : editedParagraph) {
//			allSentence += sen + " ";
//		}
//		for (Comment pComment : pComments) {
//			allSentence += pComment.getPCommentText() + " ";
//		}
//
//		String[] noteWordText = splitWord(allSentence); // 띄어쓰기 단위로 단어 구분.
//		for (String word : noteWordText) {
//			wordSet.add(word);
//		}
//		return new ArrayList<String>(wordSet); // word 사전 구성.(정렬)
//	}
//
//	private static double cosineSimilarity(int[] textA, int[] textB) { // 유사도 계산
//		int innerProduct = 0;
//		double normA, normB;
//		int sumOfSquare = 0;
//
//		for (int i = 0; i < textA.length; i++) {
//			innerProduct += textA[i] * textB[i];
//		}
//		for (Integer value : textA) {
//			sumOfSquare += value * value;
//		}
//		normA = Math.sqrt(sumOfSquare);
//
//		sumOfSquare = 0;
//		for (Integer value : textB) {
//			sumOfSquare += value * value;
//		}
//		normB = Math.sqrt(sumOfSquare);
//
//		return innerProduct / (normA * normB);
//	}
//
//	@SuppressWarnings("unchecked")
//	public static List<Integer> sortByValue(final Map<Integer, Double> map) {
//		List<Integer> list = new ArrayList<Integer>();
//		list.addAll(map.keySet());
//		Collections.sort(list, new Comparator<Object>() {
//			public int compare(Object o1, Object o2) {
//				Object v1 = map.get(o1);
//				Object v2 = map.get(o2);
//				return ((Comparable<Object>) v1).compareTo(v2);
//			}
//		});
//		Collections.reverse(list); // 주석시 오름차순
//		return list;
//	}

}
