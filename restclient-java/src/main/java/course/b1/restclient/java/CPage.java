package course.b1.restclient.java;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Helper class used with the RestTemplate for
 * reading Page objects from the HTTP response body. It does not implement
 * any iteration methods. Use it only for getting the content with {@link #getContent()}.
 * Spring framework that handles conversion to and from Java does not accept interfaces 
 * (like org.springframework.data.domain.Page) so we need to define this dummy implementation
 * that's only used as a wrapper for the actual content.
 */
public class CPage<T> implements Page<T> {
	private static final long serialVersionUID = 7305681882632809105L;
    private int number;
    private int size;
    private int totalPages;
    private int numberOfElements;
    private long totalElements;
    private boolean previousPage;
    private boolean firstPage;
    private boolean nextPage;
    private boolean lastPage;
    private List<T> content;
	
	public CPage() {
		super();
		this.content = new ArrayList<T>();
	
	}

	@Override
	public int getNumber() {
		return this.number;
	}

	@Override
	public int getSize() {
		return this.size;
	}

	@Override
	public int getNumberOfElements() {
		return this.numberOfElements;
	}

	@Override
	public List<T> getContent() {
		return this.content;
	}

	@Override
	public boolean hasContent() {
		return this.content.size() > 0;
	}

	@Override
	public Sort getSort() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public boolean isFirst() {
		return this.firstPage;
	}

	@Override
	public boolean isLast() {
		return this.lastPage;
	}

	@Override
	public boolean hasNext() {
		return this.nextPage;
	}

	@Override
	public boolean hasPrevious() {
		return this.previousPage;
	}

	@Override
	public Pageable nextPageable() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Pageable previousPageable() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Iterator<T> iterator() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public int getTotalPages() {
		return this.totalPages;
	}

	@Override
	public long getTotalElements() {
		return this.totalElements;
	}

	@Override
	public <U> Page<U> map(Function<? super T, ? extends U> converter) {
		throw new RuntimeException("Not implemented");
	}
	
}
