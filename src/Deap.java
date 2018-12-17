
public class Deap {
	int[] deap; // Deap
	int n = 0; // deap에 있는 원소의 개수; 루트는 비어 있다.

	public Deap(int maxSize) { // 생성자
		deap = new int[maxSize];
	}

	// deap의 크기를 i로 증가시키고 기존의 원소를 복사한다.
	private void increaseheap(int i) {
		int[] temp = new int[deap.length];
		System.arraycopy(deap, 0, temp, 0, deap.length);
		deap = new int[i];
		System.arraycopy(temp, 0, deap, 0, temp.length);
	}

	// 인덱스 i가 min-heap에 위치해 있으면 true를 리턴하고, 그렇지 않으면 false를 리턴한다
	private boolean inMinHeap(int i) {
		while(i > 2) {
			// 인덱스 i에 대한 원소가 minHeap에 있는 원소라면 그의 부모를 쭉 타고 올라가면 최종에는 인덱스가 1이 나오는 것임을 이용
			i = (i-1) / 2;
		}

		return (i == 1) ? true : false; // i가 1이면 true 그렇지 않으면 flase 반환
	}

	// 인덱스 i가 min-heap에 위치해 있을때 max partner의 인덱스를 리턴한다
	private int maxPartner(int i) {
		int temp = (int)(Math.log(i+1) / Math.log(2)); // log2 (i+1)
		int j = i + (int) Math.pow(2, temp - 1); // i+ 2^((log2 (i+1))-1)

		if(j > n)
			j = (j-1)/2; // j의 계산값이 현재 원소의 개수보다 더 클 때 그 부모의 인덱스에 해당하는 값을 j에 저장

		return j; // j값 반환
	}

	// 인덱스 i가 max-heap에 위치해 있을때 min partner의 인덱스를 리턴한다
	private int minPartner(int i) {
		int temp = (int) (Math.log(i+1) / Math.log(2));  // log2 (i+1)
		int j = i - (int) Math.pow(2, temp - 1); // i - 2^((log2 (i+1))-1)

		return j; // j값 반환
	}

	// min-heap에 있는 인덱스 at 위치에 key를 삽입
	private void minInsert(int at, int key) {
		// min-heap에 key삽입
		deap[at] = key; // 해당 위치에 key삽입
		int k = (at - 1) / 2;
		while(at > 1) { // 부모와 비교하며 값을 정렬 반복
			if(deap[k] > deap[at]) {
				int temp = deap[at];
				deap[at] = deap[k];
				deap[k] = temp;
			}
			at = k;
			k = (k - 1) / 2;
		}
	}

	// max-heap에 있는 인덱스 at 위치에 key를 삽입
	private void maxInsert(int at, int key) {
		// max-heap에 key삽입
		deap[at] = key; // 해당 위치에 key삽입
		int k = (at - 1) / 2;
		while(at > 2) {
			// 부모와 비교하며 값을 정렬 반복
			if(deap[k] < deap[at]) {
				int temp = deap[at];
				deap[at] = deap[k];
				deap[k] = temp;
			}
			at = k;
			k = (k - 1) / 2;
		}
	}

	// max 값을 삭제하여 리턴한다
	public int deleteMax() {
		// max-heap의 맨 위 원소를 삭제하며 반환
		if(deap[2] == 0) {
			// max-heap이 빈 경우 삭제할 수 없음을 알린다.
			System.out.println("MaxHeap에 원소가 없어 삭제할 수 없습니다.");
			return 0; // 삭제 실패
		}
		int temp = deap[n]; // 트리의 맨 끝의 원소를 temp에 저장
		deap[n] = 0; // 맨 끝의 원소를 삭제
		int rootNumber = 2; // max-heap에서의 Root Index
		int return_Value = deap[2]; // 삭제할 값
		--n; // n감소
		for(; 2*rootNumber+2 < n+1 && (deap[2*rootNumber+1] != 0 || deap[2*rootNumber+2] != 0); ) {
			/* max-heap에서의 root부터 시작해서 그 자식들 중 큰 값을 부모의 key로 올리고 그 자식에 대한 key값을 0으로 만들어줌
			 * 그것을 리프노드가 될 때까지 반복한다.
			 */
			if(deap[2*rootNumber+1] < deap[2*rootNumber+2]) {
				deap[rootNumber] = deap[2*rootNumber+2];
				rootNumber = 2*rootNumber+2;
				deap[rootNumber] = 0;
			}
			else {
				deap[rootNumber] = deap[2*rootNumber+1];
				rootNumber = 2*rootNumber+1;
				deap[rootNumber] = 0;
			}
		}
		int partner = minPartner(rootNumber); // 리프노드에 대한 minpartner
		int x = 0; // 인덱스를 받을 변수

		// minpartner가 자식이 있는 경우, 그 자식들 중 큰 값의 인덱스를 x에 저장. 자식이 없는 경우 minpartner 인덱스값을 x에 저장
		if((2*partner+1 < n+1 || 2*partner+2 < n+1) && (deap[2*partner+1] != 0 || deap[2*partner+2] != 0)) {
			if(deap[2*partner+1] < deap[2*partner+2])
				x = 2*partner+2;
			else
				x = 2*partner+1;
		}
		else
			x = partner;

		if(deap[x] > temp) {
			// x위치의 원소가 temp보다 큰 경우 x위치의 원소 값을 리프노드에 넣어주고 x위치에 temp를 minInsert한다
			deap[rootNumber] = deap[x];
			deap[x] = 0;
			minInsert(x,temp);
		}
		else
			maxInsert(rootNumber, temp); // 그렇지 않으면 리프노드에 temp를 maxInsert한다

		return return_Value; // 삭제 전 max-heap의 root원소 반환
	}
	// min 값을 삭제하여 리턴한다
	public int deleteMin() {
		if(deap[1] == 0) {
			// min-heap에 원소가 없는 경우 삭제할 수 없음을 알린다.
			System.out.println("MinHeap에 원소가 없어 삭제할 수 없습니다.");
			return 0; // 삭제 실패
		}


		int temp = deap[n]; // 트리의 맨 끝의 원소를 temp에 저장
		deap[n] = 0; // 맨 끝의 원소 삭제
		int rootNumber = 1; // min-heap의 Root Index
		int return_Value = deap[1]; // min-heap에서의 Root값 저장
		--n; // n감소

		for(;(2*rootNumber+1 < n+1 || 2*rootNumber+2 < n+1)  && (deap[2*rootNumber+1] != 0 || deap[2*rootNumber+2] != 0); ) {
			/* min-heap에서의 root부터 시작하여 그 자식들 중 작은 값을 부모의 key로 올리고 그 자식에 대한 key값을 0으로 만들어줌
			 * 그것을 리프노드가 될 때까지 반복한다.
			 */
			if(deap[2*rootNumber+1]  > deap[2*rootNumber+2]) {
				deap[rootNumber] = deap[2*rootNumber+2];
				rootNumber = 2*rootNumber+2;
				deap[rootNumber] = 0;
			}
			else {
				deap[rootNumber] = deap[2*rootNumber+1];
				rootNumber = 2*rootNumber+1;
				deap[rootNumber] = 0;
			}
		}
		int partner = maxPartner(rootNumber); // 리프노드에 대한 maxPartner값 저장



		if(temp > deap[partner]) {
			/* temp가 리프노드의 maxPartner위치에 해당하는 노드의 key값보다 크면 리프노드에 파트너의 key값을 넣어주고
			 * partner위치에 temp값을 maxInsert한다
			 */
			deap[rootNumber] = deap[partner];
			maxInsert(partner, temp);
		}
		else
			// 그렇지 않은 경우 리프노드에 temp 삽입
			deap[rootNumber] = temp;

		return return_Value;
	}

	// x를 삽입한다
	public void insert(int x) {
		if (n == deap.length - 1) {
			System.out.println("The heap is full. The heap size is doubled");
			increaseheap(deap.length * 2);
		}
		n++; // n증가

		if (n == 1) {
			// n이 1인 경우 x를 Index 1 위치에 삽입
			deap[1] = x;
			return;
		}
		if (inMinHeap(n)) { // 삽입해야할 위치가 MinHeap인지 판별
			// MinHeap인 경우 n에 대한 maxPartner의 key값과 삽입할 값을 비교하고 연산 수행
			int i = maxPartner(n);
			if (x > deap[i]) {
				// x가 maxPartner의 key값보다 큰 경우
				deap[n] = deap[i];
				maxInsert(i, x);
			} else {
				// x가 minPartner의 key값보다 크지 않은 경우
				minInsert(n, x);
			}
		} else {
			// 삽입해야할 위치가 MaxHeap인 경우 n에 대한 minPartner의 key값과 삽입할 값을 비교하고 연산 수행
			int i = minPartner(n);
			if (x < deap[i]) {
				// x가 minPartner의 key값보다 작은 경우
				deap[n] = deap[i];
				minInsert(i, x);
			} else {
				// x가 minPartner의 key값보다 작지 않은 경우
				maxInsert(n, x);
			}
		}
	}

	// deap을 프린트한다
	public void print() {
		int levelNum = 2;
		int thisLevel = 0;
		int gap = 8;
		for (int i = 1; i <= n; i++) {
			for (int j = 0; j < gap - 1; j++) {
				System.out.print(" ");
			}
			if (thisLevel != 0) {
				for (int j = 0; j < gap - 1; j++) {
					System.out.print(" ");
				}
			}
			if (Integer.toString(deap[i]).length() == 1) {
				System.out.print(" ");
			}
			System.out.print(deap[i]);
			thisLevel++;
			if (thisLevel == levelNum) {
				System.out.println();
				thisLevel = 0;
				levelNum *= 2;
				gap /= 2;
			}
		}
		System.out.println();
		if (thisLevel != 0) {
			System.out.println();
		}
	}

	public static void main(String[] argv) {
		Deap a = new Deap(10); // 크기가 10인 Deap객체 생성
		int i = 0;
		int[] data = { 4, 65, 8, 9, 48, 55, 10, 19, 20, 30, 15, 25, 50 }; // 정수형 배열 data
		for (i = 0; i < 9; i++) {
			// data(정수형 배열)의 값을 하나씩 삽입한다.
			a.insert(data[i]);
		}

		System.out.println("initial Deap");
		a.print(); // Deap출력

		for (; i < data.length; i++) {
			// data(정수형 배열)의 값을 하나씩 삽입한다.(여기서 Deap의 확장이 일어남)
			a.insert(data[i]);
		}

		System.out.println("enlarged Deap");
		a.print(); // Deap출력

		// 아래는 Delete를 실행하며 Deap을 계속 출력한다.
		  System.out.println("delete Min"); a.deleteMin(); a.print();

		  System.out.println("delete Min"); a.deleteMin(); a.print();

		  System.out.println("delete Min"); a.deleteMin(); a.print();

		  System.out.println("delete Max"); a.deleteMax(); a.print();

		  System.out.println("delete Max"); a.deleteMax(); a.print();

		  System.out.println("delete Max"); a.deleteMax(); a.print();
	}
}
