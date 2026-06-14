package ra.hwss1301.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ra.hwss1301.model.dto.request.EmployeeDTO;
import ra.hwss1301.model.dto.request.EmployeeUpdateDTO;
import ra.hwss1301.model.entity.Employee;
import ra.hwss1301.repository.EmployeeRepository;
import ra.hwss1301.service.EmployeeService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final Cloudinary cloudinary;

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee createEmployee(EmployeeDTO employeeDTO) {
        MultipartFile file = employeeDTO.getAvatarFile();
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Lỗi tải file");
        }
        String cloudinaryUrl="";
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            cloudinaryUrl = uploadResult.get("secure_url").toString();
        }
        catch (Exception e) {
            throw new RuntimeException("Lỗi: " + e.getMessage());
        }
        Employee employee = Employee.builder()
                .fullName(employeeDTO.getFullName())
                .salary(employeeDTO.getSalary())
                .email(employeeDTO.getEmail())
                .department(employeeDTO.getDepartment())
                .avatarUrl(cloudinaryUrl)
                .build();
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Long id, EmployeeUpdateDTO employeeUpdateDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên có ID " + id));
        employee.setFullName(employeeUpdateDTO.getFullName());
        employee.setEmail(employeeUpdateDTO.getEmail());
        Optional<MultipartFile> file = employeeUpdateDTO.getAvatarFile();
        if (file != null && file.isPresent() && !file.get().isEmpty()) {
            String cloudinaryUrl="";
            try {
                Map uploadResult = cloudinary.uploader().upload(file.get().getBytes(), ObjectUtils.emptyMap());
                cloudinaryUrl = uploadResult.get("secure_url").toString();
                employee.setAvatarUrl(cloudinaryUrl);
            }
            catch (Exception e) {
                throw new RuntimeException("Lỗi: " + e.getMessage());
            }
        }
        return employeeRepository.save(employee);
    }
}
