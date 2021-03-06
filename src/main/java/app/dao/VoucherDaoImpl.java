package app.dao;

import app.dto.FilterDate;
import app.entity.Voucher;
import app.exeption.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class VoucherDaoImpl implements VoucherDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public Voucher save(Voucher voucher) throws DataIntegrityViolationException {
        entityManager.persist(voucher);
        return voucher;
    }

    @Override
    public Voucher get(int id) throws EntityNotFoundException {
        Voucher voucher = entityManager.find(Voucher.class, id);
        if (voucher == null) {
            throw new EntityNotFoundException(Voucher.class);
        }
        return voucher;
    }

    @Override
    @Transactional
    public Voucher update(int id, Voucher voucherRequest) throws EntityNotFoundException {
        Voucher voucher = this.get(id);
        voucher.setEmitter(voucherRequest.getEmitter());
        voucher.setReceiver(voucherRequest.getReceiver());
        voucher.setDetail(voucherRequest.getDetail());
        voucher.setLocalDate(voucherRequest.getLocalDate());
        voucher.setPrice(voucherRequest.getPrice());
        voucher.setNumberNight(voucherRequest.getNumberNight());
        entityManager.merge(voucher);
        return voucher;
    }

    @Override
    @Transactional
    public Voucher delete(int id) throws EntityNotFoundException {
        Voucher voucher = this.get(id);
        entityManager.remove(voucher);
        return voucher;
    }

    @Override
    public List<Voucher> list(String filter, FilterDate filterDate) {
        if(filterDate.getInitialDate() != null && filterDate.getFinishDate() != null) {
            if (filter.equals("all")) {
                return entityManager.createQuery("SELECT r FROM Voucher r WHERE r.localDate >= :start AND r.localDate <= :end", Voucher.class)
                        .setParameter("start", filterDate.getInitialDate())
                        .setParameter("end", filterDate.getFinishDate())
                        .getResultList();//CHECK!!
            }
            return entityManager.createQuery("SELECT r FROM Voucher r WHERE r.detail LIKE CONCAT('%',:searchKeyword, '%') AND r.localDate >= :start AND r.localDate <= :end", Voucher.class)
                    .setParameter("start", filterDate.getFinishDate())
                    .setParameter("end", filterDate.getFinishDate())
                    .getResultList();//CHECK!!

        }
        if (filter.equals("all")) {
            return entityManager.createQuery("FROM Voucher", Voucher.class).getResultList();//CHECK!!
        }
        return entityManager.createQuery("SELECT r FROM Voucher r WHERE r.detail LIKE CONCAT('%',:searchKeyword, '%')", Voucher.class)
                .setParameter("searchKeyword", filter)
                .getResultList();
    }
}
