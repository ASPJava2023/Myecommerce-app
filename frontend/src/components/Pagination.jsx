import './Pagination.css';

const Pagination = ({ currentPage, totalPages, onPageChange }) => {
    const pages = [];
    const maxPagesToShow = 5;

    let startPage = Math.max(0, currentPage - Math.floor(maxPagesToShow / 2));
    let endPage = Math.min(totalPages - 1, startPage + maxPagesToShow - 1);

    if (endPage - startPage < maxPagesToShow - 1) {
        startPage = Math.max(0, endPage - maxPagesToShow + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
        pages.push(i);
    }

    if (totalPages <= 1) return null;

    return (
        <div className="pagination">
            <button
                className="pagination-btn"
                onClick={() => onPageChange(currentPage - 1)}
                disabled={currentPage === 0}
            >
                ← Previous
            </button>

            <div className="pagination-numbers">
                {startPage > 0 && (
                    <>
                        <button
                            className="pagination-number"
                            onClick={() => onPageChange(0)}
                        >
                            1
                        </button>
                        {startPage > 1 && <span className="pagination-ellipsis">...</span>}
                    </>
                )}

                {pages.map((page) => (
                    <button
                        key={page}
                        className={`pagination-number ${page === currentPage ? 'active' : ''}`}
                        onClick={() => onPageChange(page)}
                    >
                        {page + 1}
                    </button>
                ))}

                {endPage < totalPages - 1 && (
                    <>
                        {endPage < totalPages - 2 && <span className="pagination-ellipsis">...</span>}
                        <button
                            className="pagination-number"
                            onClick={() => onPageChange(totalPages - 1)}
                        >
                            {totalPages}
                        </button>
                    </>
                )}
            </div>

            <button
                className="pagination-btn"
                onClick={() => onPageChange(currentPage + 1)}
                disabled={currentPage === totalPages - 1}
            >
                Next →
            </button>
        </div>
    );
};

export default Pagination;
