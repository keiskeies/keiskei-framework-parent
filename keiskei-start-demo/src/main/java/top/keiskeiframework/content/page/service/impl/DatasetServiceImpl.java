package top.keiskeiframework.content.page.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.content.page.entity.Dataset;
import top.keiskeiframework.content.page.repository.DatasetBenchmarkRepository;
import top.keiskeiframework.content.page.repository.DatasetLoaderRepository;
import top.keiskeiframework.content.page.repository.DatasetPaperRepository;
import top.keiskeiframework.content.page.service.IDatasetService;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/3 00:56
 */
@Service
public class DatasetServiceImpl extends ListServiceImpl<Dataset> implements IDatasetService {

    @Autowired
    private DatasetBenchmarkRepository datasetBenchmarkRepository;
    @Autowired
    private DatasetLoaderRepository datasetLoaderRepository;
    @Autowired
    private DatasetPaperRepository datasetPaperRepository;

    @Override
    public Dataset getById(String id) {

        Dataset dataset = super.getById(id);
        if (null == dataset) {
            return null;
        }
        dataset.setPapers(datasetPaperRepository.findAllByDatasetId(dataset.getDatasetId()));
        dataset.setBenchmarks(datasetBenchmarkRepository.findAllByDatasetId(dataset.getDatasetId()));
        dataset.setLoaders(datasetLoaderRepository.findAllByDatasetId(dataset.getDatasetId()));
        return dataset;
    }
}
